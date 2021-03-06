# stop-and-wait协议的总体描述：

发送端发送消息后，等待接收端的收到回复，才进行下一条消息的发送。因为是停等协议，编码只须采用0-1即可，相邻两条消息由0、1区分。



以下是编程实现的总体流程：

# 发送端：

#### 数据结构：

next_frame_to_send，当前发送的帧的编号seq，等待发送的消息队列（0，1串），消息的数据结构{s.info，s.seq（用以检验是否丢帧）}，返送状态标志位（当前帧是正确发送0、模拟出错1还是丢失2，不发送给接收端），计时器time

#### 流程：

1、 设置相关数据结构的值（ext_frame_to_send，当前发送的帧的编号seq），取出一个消息（若消息队列为空，则跳到步骤5，发送结束消息至接收端）

2、添加CRC校验码，与当前的seq构成待发送的数据结构，设置状态标志位为0

3、 每十帧再随机选择一帧修改其某一位的数字（人为制造错误），发送前进行修改，设置状态标志位为1

4、若状态标志位处于模拟出错状态，则跳过该步骤。 发送过滤程序，每十帧随机选择一帧不发送，发送时进行选择，设置状态标志位为2

5、 发送该消息，并开始计时

6、 等待接收端返回的ACKi消息，确认接收端接收无误（i == next_frame_to_send）后，将计时器清零，回到步骤一循环执行（若为结束消息的ACK消息，则退出结束程序）；若超过接收返回消息的时间（丢帧时发生的情况），则重发当前帧；若接收端发送消息错误的返回消息，则重发当前帧。

#### 输出：

步骤1：显示next_frame_to_send变量的值，以及正在发送帧的编号s.seq

根据状态标志位，在步骤5之后，显示经过过滤器后是正确发送、模拟传输出错还是模拟帧丢失（实际没有发送）

步骤6：显示接收到确认帧，确认帧的确认序号或者显示超时



# 接收端：

#### 数据结构：

frame_expected期待收到的帧的编号（初始为0），返回消息的数据结构ACK，接收消息的数据队列，接收状态标志位（成功接收为0，出错为1）

#### 流程：

1、 接收发送端传来的消息，若为结束消息则停止接收。

2、 进行检验：s.seq与frame_expected是否相等（丢帧时不相等，实际上不会出现不等的情况，客户端发现超时会立马重传），CRC校验（出错）

3、 根据检验的结果，向发送端回传不同消息。若为有效信息，则将解析后的信息（CRC还原）添加至接收数据队列，并返回ACKi（i为期待接收到的帧编号）；若为出错，则返回错误指示消息并请求重传。

4、返回消息后，重新设置frame_expected的值，回到步骤一重复该循环。

#### 输出：

步骤1：显示frame_expected变量的值，

步骤2：接收帧是否出错（CRC余数是否为零），正确则显示接收帧的发送帧序号

步骤4：显示发送回确认帧，以及确认帧的确认序号



ps：具体编程实现时，可以加入需要用到的数据结构。



ACK1 表示“0 号帧已收到，现在期望接收的下一帧是 1 号帧”；

ACK0 表示“1 号帧已收到，现在期望接收的下一帧是 0 号帧”。



Todo（待更新）：

1. 具体参数（超时等待时间）的设置
2. （可选实现）接收端返回消息ACK丢失的情况，此时发送端重传当前帧，而接收端需要识别出该帧为重复消息，并发送ACK。