# 实验二、实现透明传输程序

## 一、实验内容
分别实现零比特填充和字节填充。

比特填充配置文件关键要点：

待发送的数据信息二进制比特串（32位）

InfoString1=0110XXXXXX11111111111XXXXXXXX110

帧起始和结束标志二进制比特串

FlagString=01111110

比特填充程序运行屏幕输出关键要点：

屏幕显示帧起始标志、帧数据信息和帧结束表示

比特填充，显示比特填充后的发送帧

显示比特删除后的接收帧

字节填充配置文件关键要点：

待发送的数据信息十六进制串（64位）

InfoString1=347D7E807E40AA7D

帧起始和结束标志十六进制串

FlagString=7E

字节填充程序运行屏幕输出关键要点：

屏幕显示帧起始标志、帧数据信息和帧结束表示

字节填充，显示字节填充后的发送帧

显示字节删除后的接收帧

## 二、零比特填充实验过程

### 1. 填充字符串（寻找连续5个1，在后面添加1个比特0）

函数输入：待填充字符串

函数输出：填充后字符串

```C++
function ZeroBitStuffing(String InfoString1) {
    String sentString = "01111110";
    int one_cnt = 0, ist_cnt = 0;
    for(int i = 0; i < InfoString1.length; i++) {
        char ch = InfoString1[i];
        sentString.append(ch);
        if(ch == '1') {
            one_cnt++;
            if(one_cnt == 5) {
                sentString.append('0');
                one_cnt = 0;
                ist_cnt++;
            }
        }
        else {
            one_cnt = 0; 
        }
    }
    return sentString + "01111110";    //拼接结束标记
}
```

### 2. 获取开始结束标记在字符串中索引

函数输入：输入字符串，开始结束标记字符串

函数输出：原始字符串开始结束位置

```C++
function GetFlagIndex(String receivedString, String flagString) {
    int start_index = receivedString.findSubString(flagString, 0) + flagString.length;
    int end_index = receivedString.findSubString(flagString, start_index);
    return start_index, end_index;    
}
```