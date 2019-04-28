// ConsoleApplication1.cpp : 定义控制台应用程序的入口点。
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

long long int f(string s)//translate a binary string to long
{
	long long int a = 0;
	for (int i = 0; i < s.length(); i++)
	{
		a = a * 2 + (s[i] - '0');
	}
	return a;
}

string t(long long int a, int length)//translate a long to binary 
{
	string s;
	while (length)
	{
		string temp = "1";
		temp[0] = a % 2 + '0';
		s = temp + s;
		a /= 2;
		length--;
	}
	return s;
}

long long GetRemainder(string newString, string GenXString) {//形成CRC
	int r = GenXString.length() - 1;
	string subStr = newString.substr(0, r);
	long long mod = f(subStr);
	long long divisor = f(GenXString);
	for (int i = r; i < newString.length(); i++) {
		mod = (mod << 1) + newString[i] - 48;
		if (mod & 0x10000) {
			mod = mod ^ divisor;
		}
		else {
			mod = mod ^ 0;
		}
	}
	return mod;
}

string GetSendString(string newString, long long mod) {//验证CRC正确
	string sendString = newString + t(mod, 16);
	cout << sendString << endl;
	return sendString;
}

int main()
{
	//从配置文件中读取InfoString1及GenXString 
	LPTSTR IpPath=new char[MAX_PATH];
	strcpy(IpPath,".\\CRC.ini");
	LPTSTR a=new char[100];
	LPTSTR b=new char[100];
	GetPrivateProfileString("Strings","InfoString1","",a,100,IpPath);
	GetPrivateProfileString("Strings","GenXString","",b,100,IpPath);
	//cout<<a<<endl;
	//cout<<b<<endl;
	string InfoString1;
	string GenXString;
	InfoString1=(string)(LPCTSTR)a;
	GenXString=(string)(LPCTSTR)b;
	
	
	string InfoString2;
	const int CRCLength = 16;//数值为GenXString.length()-1;
	srand(time(0));
	//for (int i = 0; i < 25; i++)
	//{
	//	InfoString1 += rand() % 2 + '0';
	//}
	cout << "CRC-CCITT二进制比特串："<<GenXString << endl<<endl;
	cout<<"待发送的数据信息二进制比特串："<<InfoString1<<endl;
	string newString = InfoString1;
	for (size_t i = 0; i < CRCLength; i++)
	{
		newString += "0";
	}
	long long mod = GetRemainder(newString, GenXString);
	cout << "生成循环冗余校验码CRC-Code："<<(bitset<CRCLength>)mod << endl;
	string SendString = GetSendString(InfoString1, mod);
	cout << "带校验和的发送帧："<<SendString << endl<<endl;
	//cout << (bitset<CRCLength>)mod << endl;
	cout << "接收数据信息二进制比特串："<<SendString << endl;
	long long Mod = GetRemainder(SendString, GenXString);
	cout << "余数："<<Mod << endl;
	if (Mod == 0) {
		cout << "校验无错" << endl;
	}
	else {
		cout << "校验失败" << endl;
	}
	system("pause");
}
