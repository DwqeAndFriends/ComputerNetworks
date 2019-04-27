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
function ZeroBitGetFlagIndex(String receivedString, String flagString) {
    int start_index = receivedString.findSubString(flagString, 0) + flagString.length;
    int end_index = receivedString.findSubString(flagString, start_index);
    return [start_index, end_index];    
}
```

## 3. 获取原始字符串

函数输入：接收到的字符串

函数输出：原始字符串

```C++
function ZeroBitGetOriginalString(String receivedString) {
    int[] Index = ZeroBitGetFlagIndex(receivedString, "01111110");
    String tmpString = receivedString.substring(Index[0], Index[1]);
    int delIndex = tmpString.findSubString("11111", 0);    //获取子串开始索引
    while(delIndex != -1) {
        tmpString.deleteCharAt(delIndex + 5);    //删除指定元素
        delIndex = tmpString.findSubString("11111", delIndex + 5);
    }
    return tmpString;
}
```

## 三、字节填充实验过程

### 1. 填充字节（添加开始结束标记；在原始字符串中的转义字符，开始结束标记前添加转义字符）

函数输入：待填充字符串，开始结束标记，转义字符（串）

函数输出：可发送字符串

```C++
function PPPByteStuffing(String InfoString1, String flagString, String ESC) {
    String newString;
    for (int i = 0; i < InfoString1.length; i++) {    //在原始字符串中添加转义字符
        if(InfoString1[i] == flagString[0] && InfoString1[i + 1] == flagString.[1]) {    //开始结束标记前添加转义字符
            newString.append(ESC);
            newString.append(flagString);
            ++i;
        }
        else if(InfoString1[i] == ESC[0] && InfoString1[i + 1] == ESC[0]) {    //转义字符前添加转义字符
            newString.append(ESC);
            newString.append(ESC);
            ++i;
        }
        else {
            newString.append(InfoString1[i]);
        }
    }
    return flagString + newString + flagString;    //添加开始结束标记
}
```

### 2. 去除填充，及开始结束标记

函数输入：接收到的字符串，开始结束标记，转义字符（串）

函数输出：原始字符串

```C++
ByteGetOriginalString(String receivedString, String flagString, String ESC) {
    String originalString;
    bool startFlag = false;
    for(int i = 0; i < receivedString.length; i++) {
        if(receivedString[i] == ESC[0] && receivedString[i + 1] == ESC[1]) {    //忽略转义字符
            if(startFlag) {
                originalString.append(receivedString[i + 2]);
                originalString.append(receivedString[i + 3]);
            }
            i += 3;
        }
        else if(receivedString[i] == flagString[0] && receivedString[i + 1] == flagString[1]) {    //判断开始结束标记
            if(!startFlag) {
                startFlag = true;
                ++i;
            }
            else {
                break;
            }
        }
        else {
            if(startFlag) {
                originalString.append(receivedString[i]);
            }
        }
    }
    return originalString;
}
```