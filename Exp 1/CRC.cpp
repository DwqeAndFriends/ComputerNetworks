#include<iostream>
#include<string>
#include<sstream>
#include<fstream>
#include<bitset>
#include<ctime>
#include<math.h>
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

string t(long long int a)//translate a long to binary 
{
	string s;
	while (a)
	{
		string temp = "1";
		temp[0] = a % 2 + '0';
		s = temp + s;
		a /= 2;
	}
	return s;
}

long long int GetRemainder(string newString, string GenXString)
{
	int r = GenXString.length() - 1;
	string subString = newString.substr(0, r);
	long long int mod = f(subString);
	long long int divisor = f(GenXString);
	for (int i = r; i < newString.length(); i++)
	{
		mod = mod * 2 + (newString[i] - '0');
		if (mod >= pow(2, r))
		{
			mod ^= divisor;
		}
		else
		{
			mod ^= 0;
		}
	}
	return mod;
}

string GetSendString(string InfoString1, string GenXString)
{
	int rr = GenXString.length() - 1;
	string newString = InfoString1;
	for (int i = 0; i < rr; i++)
	{
		newString = newString + "0";
	}
	long long mod = GetRemainder(newString, GenXString);
	long long tmp = f(newString);
	//cout << newString << endl;
	//cout << mod << endl;
	//cout << tmp << endl;
	cout << (mod^tmp) << endl;
	return t(tmp^mod);
}

void main()
{
	string InfoString1 = "1101011111";
	string GenXString = "10011";
	string SendString = GetSendString(InfoString1, GenXString);
	cout << SendString << endl;
	long long cmp = GetRemainder(SendString, GenXString);
	cout << cmp << endl;
	system("pause");
}
