// ConsoleApplication1.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include<iostream>
#include<string>
#include<fstream>
#include<bitset>
#include<ctime>
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

string tobyte(string mem)
{
	string res;
	for (int i = 0; i < mem.length(); i++)
	{
		res += t(mem[i], 8);
	}
	return res;
}

long long GetRemainder(string newString, string GenXString) {
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

string GetSendString(string InfoString1, string GenXString) //形成CRC
{
	string newString = InfoString1;
	for (size_t i = 0; i < GenXString.length() - 1; i++)
	{
		newString += "0";
	}
	long long mod = GetRemainder(newString, GenXString);
	string sendString = InfoString1 + t(mod, 16);
	cout << sendString << endl;
	return sendString;
}

bool isCRC(string InfoString2, string GenXString)//验证CRC正确
{
	long long mod = GetRemainder(InfoString2, GenXString);
	if (mod == 0) {
		return true;
	}
	else {
		return false;
	}
}

int main()
{
	string InfoString1, GenXString;
	const int CRCLength = 16;//数值为GenXString.length()-1;
	InfoString1 = "Hello World";
	GenXString = "10001000000100001";
	cout << InfoString1 << endl;
	string newString = tobyte(InfoString1);
	string SendString = GetSendString(newString, GenXString);
	cout << "Send " << SendString << endl;
	cout <<"Receive "<< SendString << endl;
	if (isCRC(SendString, GenXString))
	{
		cout << "Success" << endl;
	}
	else
	{
		cout << "Error" << endl;
	}
	system("pause");
}
