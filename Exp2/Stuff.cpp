// Stuff.cpp : 定义控制台应用程序的入口点。
//

#include "stdafx.h"
#include<string>
#include<iostream>
using namespace std;
string FlagString = "01111110";
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
	string send1="0110";
	for (int i = 0; i < 6; i++)
	{
		send1 += rand() % 2 + '0';
	}
	send1 += "11111111111";
	for (int i = 0; i < 8; i++)
	{
		send1+= rand() % 2 + '0';
	}
	send1 += "110";
	cout << "bitstuff:" << endl;
	cout << "Frame Start" << endl;
	cout << FlagString << endl;
	cout << "Frame" << endl;
	cout << send1 << endl;
	cout << "Frame End" << endl;
	cout << FlagString << endl;
	string stuff1 = bitstuff(FlagString+send1+FlagString);
	cout << "After Frame stuff" << endl;
	cout << stuff1 << endl;
	string del1 = bitdel(stuff1);
	cout << "After Frame delete" << endl;
	cout << del1 << endl;
	cout << endl;
	cout << "bytestuff:" << endl;
	string InfoString1 = "347D7E80//7E40AA7D";
	string FlagString = "7E";
	string ESC = "//";
	cout << "Frame Start" << endl;
	cout << FlagString << endl;
	cout << "Frame" << endl;
	cout << InfoString1 << endl;
	cout << "Frame End" << endl;
	cout << FlagString << endl;
	string sendstring2 = bytestuff(InfoString1, FlagString, ESC);
	cout << sendstring2 << endl;
	string recstring2 = bytedel(sendstring2, FlagString, ESC);
	cout << recstring2 << endl;
	system("pause");
}