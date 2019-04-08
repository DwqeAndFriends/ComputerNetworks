# 实验一、 循环冗余校验CRC生成和校验程序

## 实验内容

配置文件关键要点：

待发送的数据信息二进制比特串（32位）

InfoString1=0110XXXXXXXXXXXXXXXXXXXX110

收发双方预定的生成多项式采用CRC-CCITT=X16+X12+X5+1，对应的二进制比特串（17位）

GenXString=10001000000100001

接收的数据信息二进制比特串（32位）

InfoString2=0110XXXXXXXXXXXXXXXXXXXX110

程序运行屏幕输出要点：

首先显示待发送的数据信息二进制比特串

然后显示收发双方预定的生成多项式采用CRC-CCITT，对应的二进制比特串

计算循环冗余校验码CRC-Code

显示生成的CRC-Code，以及带校验和的发送帧

显示接收的数据信息二进制比特串，以及计算生成的CRC-Code

计算余数

显示余数，为零表示无错，不为零表示出错

实验采用C++，Java，Python实现，以下所有函数均可使用相同输入输出的库函数替代

C++中使用string类型进行实现

## 实验过程

### 1. 文件读取

读取配置文件中待发送字符串InfoString1



### 2. 子串获取

函数输入：输入字符串，子串开始索引，子串长度

函数输出：截取子串（不能对原始字符串有任何修改）

```c++
funtion GetSubString(String iStr, int start, int len) {
    return (String)subStr
}
```



### 3. 字符串拼接

函数输入：字符串1，字符串2

函数输出：拼接后字符串（使用库函数或重载运算符+实现）



### 4. 二进制字符串数字相互转化

原始字符串长度为32位，拼接后字符串长度大于32位，因此采用long long型存储数字

函数输入：二进制字符串

函数输出：long long型数字

```c++
function String2LL(String str) {
    return (long long)num
}
```



函数输入：long long型数字

函数输出：二进制字符串

```C++
function LL2String(long long num) {
    return (String)str
}
```



### 5. 异或运算

使用^实现



### 6. 余数计算

函数输入：待发送字符串InfoString1， 生成多项式字符串GenXString

函数输出：余数

```C++
function GetRemainder(String newString, String GenXString) {
    int r = GenXString.length-1;
    String subStr = GetSubString(newString, 0, r);    //获取newString，从0开始长度为r的子串subStr
    long long mod = String2LL(subString);
    long long divisor = String2LL(GenXString);
    for(int i = r; i < newString.length; i++) {
        mod = mod + String2Int(newString[i]);
        if(mod > pow(2, r)) {
            mod = mod ^ divisor;
        }
        else {
            mod = mod ^ 0;
        }
    }
    return mod;
}
```



### 7. 生成发出帧

函数输入：待发送字符串InfoString1，计算所得余数

函数输出：发出帧

```C++
function GetSendString(String newString, long long mod) {
    long long ins = String2LL(newString) + mod;
    String sendString = LL2String(ins);
    return sendString
}
```



### 8. 主函数

```C++
function main() {
    if(发送) {
        String newString = InfoString1 + '0' * r;    //字符串拼接，在InfoString1后拼接上，r个0，得到newString
        long long Mod = GetRemainder(newString, GenXString);
        String SendString = GetSendString(newString, Mod);
    }
    else if(接收) {
        long long Mod = GetRemainder(InfoString2, GenXString);
        if(Mod == 0) {
            输出正确;
        }
        else {
            输出错误；
        }
    }
}
```

