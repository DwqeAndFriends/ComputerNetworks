// Stuff.cpp : 定义控制台应用程序的入口点。
//

#include<iostream>
#include<string>
#include<fstream>
#include<bitset>
#include<ctime>
#include<windows.h>
#include<comutil.h>
#include<tchar.h>
using namespace std;
string FlagString;
string bitstuff(string InfoString1) {
	string newString;
	int count=0;
	for (int i = 0; i < InfoString1.length(); i++)
	{
		newString += InfoString1[i];
		if (InfoString1[i] == '1')
		{
			count++;
			if (count == 5)
			{
				newString += '0';
				count = 0;
			}
		}
		else
		{
			count = 0;
		}
	}
	return newString;
}

string bitdel(string InfoString1)
{
	string newString;
	int count = 0;
	for (int i = 0; i < InfoString1.length(); i++)
	{
		if (InfoString1[i] == '0')
		{
			if (count != 5)
			{
				newString += InfoString1[i];
			}
			count = 0;
		}
		else
		{
			count++;
			newString += InfoString1[i];
		}
	}
	return newString.substr(FlagString.length(), newString.length() - 2 * FlagString.length());
}

string bytestuff(string InfoString1,string Flag,string ESC)
{
	string windows,res;
	for (int i = 0; i < InfoString1.length(); i+=2)
	{
		windows = InfoString1.substr(i, 2);
		if (windows == Flag)
			res += ESC;
		if (windows == ESC)
			res += ESC;
		res += windows;
	}
	return Flag+res+Flag;
}

string bytedel(string InfoString1, string Flag, string ESC)
{
	string windows[2],res;
	bool isstart = false;
	for (int i = 0; i < InfoString1.length(); i+=2)
	{
		windows[0] = InfoString1.substr(i, 2);
		if (windows[0]==ESC)
		{
			windows[1] = InfoString1.substr(i + 2, 2);
			if (windows[1] == Flag)
			{
				res += windows[1];
				i += 2;
			}
		}
		else if (windows[0] == Flag)
		{
			if (!isstart)
			{
				isstart = true;
			}
			else
			{
				break;
			}
		}
		else
		{
			res += windows[0];
		}
	}
	return res;
}

int main()
{
	string send1;
	LPTSTR IpPath=new char[MAX_PATH];
	strcpy(IpPath,".\\Stuffing.ini");
	LPTSTR a=new char[100];
	LPTSTR b=new char[100];
	LPTSTR c=new char[100];
	
	
	cout << "字节填充：" << endl;
	string InfoString1;
	//string FlagString;
	string ESC;
	GetPrivateProfileString("ByteStuffing","InfoString1","",a,100,IpPath);
	GetPrivateProfileString("ByteStuffing","FlagString","",b,100,IpPath);
	GetPrivateProfileString("ByteStuffing","ESC","",c,100,IpPath);
	//cout<<a<<endl;
	//cout<<b<<endl;
	InfoString1=(string)(LPCTSTR)a;
	FlagString=(string)(LPCTSTR)b;
	ESC=(string)(LPCTSTR)c;
	
	cout << "帧起始结束标志：" << FlagString << endl;
	cout << "转义字符ESC：" <<ESC<< endl;
	cout << "待填充数据帧："<<InfoString1 << endl;
	string sendstring2 = bytestuff(InfoString1, FlagString, ESC);
	cout << "字节填充后发送帧：" << sendstring2 << endl<<endl;
	cout<<"包含其它字符的接收帧："<<"2312FF"+sendstring2<<endl;
	string recstring2 = bytedel(sendstring2, FlagString, ESC);
	cout << "接收帧字节删除后数据帧："<< recstring2 << endl<<endl;
	
	cout<<"---------------------------------------------------------------"<<endl<<endl;
	
	
	GetPrivateProfileString("ZeroBitStuffing","InfoString1","",a,100,IpPath);
	GetPrivateProfileString("ZeroBitStuffing","FlagString","",b,100,IpPath);
	//cout<<a<<endl;
	//cout<<b<<endl;
	send1=(string)(LPCTSTR)a;
	FlagString=(string)(LPCTSTR)b;
	
	cout << "零比特填充：" << endl;
	cout << "帧起始结束标志：" <<FlagString<< endl;
	cout << "待填充数据帧："<<send1 << endl;
	//cout << "Frame End" << endl;
	//cout << FlagString << endl;
	string stuff1 = bitstuff(FlagString+send1+FlagString);
	cout << "零比特填充后数据帧：" << stuff1 << endl<<endl;
	cout<<"包含其它字符的接收帧："<<"01100"+stuff1<<endl;
	string del1 = bitdel(stuff1);
	cout << "接收帧零比特删除后数据帧：" << del1 << endl;
	cout << endl;
}
