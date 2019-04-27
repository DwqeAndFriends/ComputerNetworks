// ConsoleApplication1.cpp : �������̨Ӧ�ó������ڵ㡣
//

#include "stdafx.h"
#include<iostream>
#include<string>
#include<sstream>
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

string GetSendString(string newString, long long mod) {
	string sendString = newString + t(mod, 16);
	cout << sendString << endl;
	return sendString;
}

int main()
{
	string InfoString1, InfoString2, GenXString;
	const int CRCLength = 16;//��ֵΪGenXString.length()-1;
	srand(time(0));
	InfoString1 = "0110";
	for (int i = 0; i < 25; i++)
	{
		InfoString1 += rand() % 2 + '0';
	}
	InfoString1 += "110";
	GenXString = "10001000000100001";
	cout << "Send" << endl;
	cout << InfoString1 << endl;
	cout << GenXString << endl;
	string newString = InfoString1;
	for (size_t i = 0; i < CRCLength; i++)
	{
		newString += "0";
	}
	long long mod = GetRemainder(newString, GenXString);
	cout << (bitset<CRCLength>)mod << endl;
	string SendString = GetSendString(InfoString1, mod);
	cout << "Receive" << endl;
	cout << SendString << endl;
	cout << (bitset<CRCLength>)mod << endl;
	long long Mod = GetRemainder(SendString, GenXString);
	cout << Mod << endl;
	if (Mod == 0) {
		cout << "Send Success" << endl;
	}
	else {
		cout << "Send Fail" << endl;
	}
	system("pause");
}