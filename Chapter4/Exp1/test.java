
 

import java.io.IOException;
import java.util.ArrayList;

import jpcap.*;
import jpcap.packet.ARPPacket;
import jpcap.packet.EthernetPacket;
import jpcap.packet.ICMPPacket;
import jpcap.packet.IPPacket;
import jpcap.packet.Packet;
import jpcap.packet.TCPPacket;
import jpcap.packet.UDPPacket;

 

public class Test {

 

	public static void main(String[] args) throws IOException {

		/*--------------	第一步绑定网络设备       --------------*/

		//获得网络接口列表
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();

		//遍历网络接口
		for (int i = 0; i < devices.length; i++) {
		  //打印它的名称和描述
		  System.out.println(i+": "+devices[i].name + "(" + devices[i].description+")");

		  //打印它的datalink的名称和描述
		  System.out.println(" datalink: "+devices[i].datalink_name + "(" + devices[i].datalink_description+")");

		  //打印它的MAC地址
		  System.out.print(" MAC address:");
		  for (byte b : devices[i].mac_address)
		    System.out.print(Integer.toHexString(b&0xff) + ":");
		  System.out.println();

		  //打印它的IP地址、子网掩码和广播地址
		  for (NetworkInterfaceAddress a : devices[i].addresses)
		    System.out.println(" address:"+a.address + " " + a.subnet + " "+ a.broadcast);
		}
		int index=7;
		JpcapCaptor captor=JpcapCaptor.openDevice(devices[index], 65535, false, 20);
		class PacketPrinter implements PacketReceiver{
			public void receivePacket(Packet packet) {
				//String str=packet.toString();
				if(packet instanceof jpcap.packet.ARPPacket) {
					ARPPacket arppacket=(ARPPacket)packet;
					System.out.println("--------------ARP包--------------");
					System.out.println("捕获到的包长度："+arppacket.caplen);
					//System.out.println("数据："+arppacket.data);
					System.out.println("以太帧报头："+arppacket.datalink);
					//System.out.println("报头数据："+arppacket.header);
					System.out.println("数据包长度："+arppacket.len);
					System.out.println("时间戳（秒）："+arppacket.sec);
					System.out.println("时间戳（微秒）："+arppacket.usec);
					System.out.println("协议类型："+arppacket.prototype);
					System.out.println("toString()方法："+arppacket.toString());
					System.out.println("硬件："+arppacket.hardtype);
					System.out.println("硬件地址长度："+arppacket.hlen);
					System.out.println("协议地址长度："+arppacket.plen);
					System.out.println("操作字段："+arppacket.operation);
					System.out.println("发送端以太地址："+arppacket.sender_hardaddr);
					System.out.println("发送端IP地址："+arppacket.sender_protoaddr);
					System.out.println("目的以太地址："+new String(arppacket.target_hardaddr));
					System.out.println("--------------------------------");

				}
				else if(packet instanceof jpcap.packet.TCPPacket) {
					TCPPacket tcppacket=(TCPPacket)packet;
					//String str=new String(packet.header);
					//System.out.println("硬件类型："+tcppacket.);
					System.out.println("--------------TCP包--------------");
					System.out.println("捕获到的包长度："+tcppacket.caplen);
					//System.out.println("数据："+tcppacket.data);
					System.out.println("以太帧报头："+tcppacket.datalink);
					//System.out.println("报头数据："+str);
					System.out.println("数据包长度："+tcppacket.len);
					System.out.println("时间戳（秒）："+tcppacket.sec);
					System.out.println("时间戳（微秒）："+tcppacket.usec);
					
					System.out.println("源端口："+tcppacket.src_port);
					System.out.println("目的端口："+tcppacket.dst_port);
					System.out.println("序号："+tcppacket.sequence);
					System.out.println("URG标志："+tcppacket.urg);
					System.out.println("ACK标志："+tcppacket.ack);
					System.out.println("PSH标志："+tcppacket.psh);
					System.out.println("RST标志："+tcppacket.rst);
					System.out.println("SYN标志："+tcppacket.syn);
					System.out.println("FIN标志："+tcppacket.fin);
					System.out.println("Windows大小："+tcppacket.window);
					System.out.println("紧急指针："+tcppacket.urgent_pointer);
					System.out.println("ToString方法："+tcppacket.toString());
					System.out.println("---------------------------------");
				}
				else if(packet instanceof jpcap.packet.UDPPacket) {
					UDPPacket udppacket=(UDPPacket)packet;
					System.out.println("--------------UDP包--------------");
					System.out.println("捕获到的包长度："+udppacket.caplen);
					//System.out.println("数据："+tcppacket.data);
					System.out.println("以太帧报头："+udppacket.datalink);
					//System.out.println("报头数据："+str);
					System.out.println("数据包长度："+udppacket.len);
					System.out.println("时间戳（秒）："+udppacket.sec);
					System.out.println("时间戳（微秒）："+udppacket.usec);
					
					System.out.println("源端口："+udppacket);
					System.out.println("目标端口："+udppacket);
					System.out.println("UDP长度："+udppacket);
					System.out.println("ToString方法："+udppacket);
					System.out.println("---------------------------------");
				}
				else if(packet instanceof jpcap.packet.IPPacket) {
					IPPacket ippacket=(IPPacket)packet;
					System.out.println("--------------IP包--------------");
					System.out.println("捕获到的包长度："+ippacket.caplen);
					//System.out.println("数据："+tcppacket.data);
					System.out.println("以太帧报头："+ippacket.datalink);
					//System.out.println("报头数据："+str);
					System.out.println("数据包长度："+ippacket.len);
					System.out.println("时间戳（秒）："+ippacket.sec);
					System.out.println("时间戳（微秒）："+ippacket.usec);
					
					System.out.println("版本："+ippacket.version);
					System.out.println("服务类型："+ippacket.rsv_tos);
					System.out.println("总长度："+ippacket.length);
					System.out.println("分组标识："+ippacket.ident);
					System.out.println("RF："+ippacket.rsv_frag);
					System.out.println("DF："+ippacket.dont_frag);
					System.out.println("MF："+ippacket.more_frag);
					System.out.println("13位片位移："+ippacket.offset);
					System.out.println("TTL："+ippacket.hop_limit);
					System.out.println("协议类型："+ippacket.protocol);
					System.out.println("源IP："+ippacket.src_ip);
					System.out.println("目的IP："+ippacket.dst_ip);
					System.out.println("IPv4选项："+ippacket.option);
					System.out.println("优先权："+ippacket.priority);
					System.out.println("ToString方法："+ippacket.toString());
					System.out.println("---------------------------------");

				}
			}
			//public void receivePacket(EthernetPacket ethernetpacket) {
				//System.out.println(ethernetpacket.toString());
			//}
		}
		//captor.setFilter("ip", true);
		captor.loopPacket(10, new PacketPrinter());
		captor.close();

	}

 
}
