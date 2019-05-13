#include"pcap.h"
#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#include<WinSock2.h>
#include <windows.h>

struct ethernet_header
{
	u_int8_t ether_dhost[6]; //目的Mac地址   
	u_int8_t ether_shost[6]; //源Mac地址   
	u_int16_t ether_type;    //协议类型   
};

struct ip_header
{
#if defined(WORDS_BIENDIAN)   
	u_int8_t   ip_version : 4,
		ip_header_length : 4;
#else   
	u_int8_t   ip_header_length : 4,
		ip_version : 4;
#endif   
	u_int8_t    ip_tos;
	u_int16_t   ip_length;
	u_int16_t   ip_id;
	u_int16_t   ip_off;
	u_int8_t    ip_ttl;
	u_int8_t    ip_protocol;
	u_int16_t   ip_checksum;
	struct in_addr ip_source_address;
	struct in_addr ip_destination_address;
};

struct tcp_header {
	u_int16_t tcp_source_port;
	u_int16_t tcp_destination_port;
	u_int32_t tcp_acknowledgement;
	u_int32_t tcp_ack;
#ifdef WORDS_BIGENDIAN
	u_int8_t tcp_offset:4,
		tcp_reserved:4;
#else
	u_int8_t tcp_reserved : 4,
		tcp_offset : 4;
#endif
	u_int8_t tcp_flags;
	u_int16_t tcp_windows;
	u_int16_t tcp_checksum;
	u_int16_t tcp_urgent_pointer;
	
};

struct udp_header {
	u_int16_t udp_source_port;
	u_int16_t udp_destination_port;
	u_int16_t  udp_length;
	u_int16_t udp_checksum;
};

struct arp_header {
	u_int16_t arp_hard;
	u_int16_t arp_type;
	u_int8_t arp_hard_length;
	u_int8_t arp_protocol_length;
	u_int16_t arp_operation;
	u_int8_t arp_source_mac[6];
	struct in_addr arp_source_ip;
	u_int8_t arp_destination_mac[6];
	struct in_addr arp_destination_ip;

};

struct icmp_header {
	u_int8_t icmp_type;
	u_int8_t icmp_code;
	u_int16_t icmp_checksum;
};

void icmp_protocol_packet_callback(u_char *argument, const struct pcap_pkthdr*
	packet_header, const u_char* packet_content) {
	struct icmp_header *icmp_protocol;
	u_short icmp_type;
	u_short icmp_code;
	u_int16_t checksum;
	icmp_type= ntohs(icmp_protocol->icmp_type);
	icmp_code = ntohs(icmp_protocol->icmp_code);
	checksum = ntohs(icmp_protocol->icmp_checksum);
	printf("\n网络层（ICMP协议)\n");
	printf("ICMP类型:\t\t%d\n", icmp_type);
	printf("ICMP代码:\t\t%d\n", icmp_code);
	printf("检验和 :\t%d\n", checksum);
}

void arp_protocol_packet_callback(u_char *argument, const struct pcap_pkthdr*
	packet_header, const u_char* packet_content) {
	struct arp_header *arp_protocol;
	arp_protocol = (struct arp_header*) (packet_content + 14);
	u_short arp_hard;
	u_short arp_type;
	u_short arp_operation;
	u_short arp_hard_length;
	u_short arp_protocol_length;
	u_char * souce_mac;
	u_char * souce_ip;
	u_char * dest_mac;
	u_char * dest_ip;

	arp_hard = ntohs(arp_protocol->arp_hard);
	arp_type = ntohs(arp_protocol->arp_type);
	arp_operation = ntohs(arp_protocol->arp_operation);
	arp_hard_length = ntohs(arp_protocol->arp_hard_length);
	arp_protocol_length = ntohs(arp_protocol->arp_protocol_length);
	souce_mac = arp_protocol->arp_source_mac;
	dest_mac = arp_protocol->arp_destination_mac;

	printf("\n网络层（ARP协议)\n");
	printf("硬件类型:\t\t%04x\n", arp_hard);
	printf("协议类型:\t\t%04x\n", arp_type);
	printf("MAC地址长度:\t\t%d\n", arp_hard_length);
	printf("IP地址长度:\t\t%d\n", arp_protocol_length);
	printf("操作码:\t\t%d\n", arp_operation);
	printf("源Mac地址:\t%02x:%02x:%02x:%02x:%02x:%02x:\n", *souce_mac, *(souce_mac + 1), *(souce_mac + 2), *(souce_mac + 3), *(souce_mac + 4), *(souce_mac + 5));
	printf("源IP:\t%s\n", inet_ntoa(arp_protocol->arp_source_ip));
	printf("目的Mac地址:\t%02x:%02x:%02x:%02x:%02x:%02x:\n", *dest_mac, *(dest_mac + 1), *(dest_mac + 2), *(dest_mac + 3), *(dest_mac + 4), *(dest_mac + 5));
	printf("目的IP:\t%s\n", inet_ntoa(arp_protocol->arp_destination_ip));
}

void udp_protocol_packet_callback(u_char *argument, const struct pcap_pkthdr*
	packet_header, const u_char* packet_content) {
	u_short source_port;
	u_short destination_port;
	u_int16_t checksum;
	struct udp_header *udp_protocol;
	udp_protocol = (struct udp_header *) (packet_content + 14 + 20);
	source_port = ntohs(udp_protocol->udp_source_port);
	destination_port = ntohs(udp_protocol->udp_destination_port);
	checksum = ntohs(udp_protocol->udp_checksum);
	printf("\n运输层（UDP协议)\n");
	printf("源端口：\t %d\n", source_port);
	printf("目的端口：\t %d\n", destination_port);


	int min = (destination_port <source_port) ? destination_port : source_port;
	printf("应用层协议是：\t");
	switch (min)
	{
	case 53:printf(" DNS 域名解析系统");
		break;

	case 161:printf(" SNMP 简单网络管理协议");
		break;

	case 69:printf(" TFTP 简单文件传输协议 ");
		break;

	

	default:printf("【其他类型】 ");
		break;
	}
	printf("\n");

	printf("报文长度 :\t%d\n", udp_protocol->udp_length);
	printf("检验和 :\t%d\n", checksum);
}

void tcp_protocol_packet_callback(u_char *argument, const struct pcap_pkthdr*
	packet_header, const u_char* packet_content)
{
	struct tcp_header *tcp_protocol;
	u_char flags;
	int header_length;
	u_short source_port;
	u_short destination_port;
	u_short windows;
	u_short urgent_pointer;
	//u_int header_length;
	u_int sequence;
	u_int acknowledgement;
	u_int16_t checksum;
	tcp_protocol = (struct tcp_header *) (packet_content + 14 + 20);
	source_port = ntohs(tcp_protocol->tcp_source_port);
	destination_port = ntohs(tcp_protocol->tcp_destination_port);
	header_length = tcp_protocol->tcp_offset*4 ;
	sequence = ntohl(tcp_protocol->tcp_acknowledgement);
	acknowledgement = ntohl(tcp_protocol->tcp_ack);
	windows = ntohs(tcp_protocol->tcp_windows);
	urgent_pointer = ntohs(tcp_protocol->tcp_urgent_pointer);
	flags = tcp_protocol->tcp_flags;
	checksum = ntohs(tcp_protocol->tcp_checksum);
	printf("\n运输层（TCP协议)\n");
	printf("源端口：\t %d\n", source_port);
	printf("目的端口：\t %d\n", destination_port);

	int min = (destination_port <source_port) ? destination_port : source_port;
	printf( "应用层协议是：\t");
	switch (min)
	{
	case 80:printf(" 超文本传输协议");
		break;

	case 21:printf(" ftp 文件传输协议");
		break;

	case 23:printf(" Telnet 服务 ");
		break;

	case 25:printf(" smtp 简单邮件传输协议");
		break;

	case 110:printf(" pop3 邮局协议版本3 ");
		break;
	case 443:printf(" https 安全超文本传输协议 ");
		break;
	case 53:printf(" DNS 域名解析系统");
		break;
	default:printf("【其他类型】 ");
		break;
	}
	printf("\n");
	printf("序列号：\t %u \n", sequence);
	printf("确认号：\t%u \n", acknowledgement);
	printf("首部长度：\t%d \n", header_length);
	printf("保留字段：\t%d \n", tcp_protocol->tcp_reserved);
	printf("控制位：");
	if (flags & 0x08) printf("\t【推送 PSH】");
	if (flags & 0x10) printf("\t【确认 ACK】 ");
	if (flags & 0x02) printf("\t【同步 SYN】");
	if (flags & 0x20) printf("\t【紧急 URG】");
	if (flags & 0x01) printf("\t【终止 FIN】");
	if (flags & 0x04) printf("\t【复位 RST】");

	printf("\n");
	printf("窗口大小 :\t%d \n", windows);
	printf("检验和 :\t%d\n", checksum);
	printf("紧急指针字段 :\t%d\n", urgent_pointer);
}

void ip_protocol_packet_callback(u_char *argument, const struct pcap_pkthdr*
	packet_header, const u_char* packet_content)
{
	struct ip_header *ip_protocol;
	u_int  header_length;
	u_int offset;
	u_char tos;
	u_int16_t checksum;
	ip_protocol = (struct ip_header*) (packet_content + 14);
	checksum = ntohs(ip_protocol->ip_checksum);
	header_length = ip_protocol->ip_header_length * 4;
	tos = ip_protocol->ip_tos;
	offset = ntohs(ip_protocol->ip_off);
	printf("\n网络层（IP协议)\n");
	printf("IP版本:\t\tIPv%d\n", ip_protocol->ip_version);
	printf("IP协议首部长度:\t%d\n", header_length);
	printf("服务类型:\t%d\n", tos);
	printf("总长度:\t\t%d\n", ntohs(ip_protocol->ip_length));
	printf("标识:\t\t%d\n", ntohs(ip_protocol->ip_id));
	printf("片偏移:\t\t%d\n", (offset & 0x1fff) * 8);
	printf("生存时间:\t%d\n", ip_protocol->ip_ttl);
	printf("首部检验和:\t%d\n", checksum);
	printf("源IP:\t%s\n", inet_ntoa(ip_protocol->ip_source_address));
	printf("目的IP:\t%s\n", inet_ntoa(ip_protocol->ip_destination_address));
	printf("协议号:\t%d\n", ip_protocol->ip_protocol);
	printf( "\n运输层协议是:\t");
	switch (ip_protocol->ip_protocol)
	{
	case 6:
		printf("TCP\n");
		tcp_protocol_packet_callback(argument, packet_header, packet_content);
		break; 
	case 17:
		printf("UDP\n");
		udp_protocol_packet_callback(argument, packet_header, packet_content);
		break;
	case 1:
		printf("ICMP\n");

		break;
	/*case 2:
		printf("IGMP\n");
		break;*/
	default:break;
	}
}

void ethernet_protocol_packet_callback(u_char *argument, const struct pcap_pkthdr *packet_header, const u_char* packet_content)
{
	
	u_short ethernet_type;
	struct ethernet_header *ethernet_protocol;
	u_char *mac_string;
	static int packet_number = 1;
	printf("第%d个数据包\n", packet_number);
	printf("\n链路层(以太网协议)\n");
	ethernet_protocol = (struct ethernet_header *) packet_content; 
	printf("以太网类型为 :\t");
	ethernet_type = ntohs(ethernet_protocol->ether_type); /*获得以太网类型*/
	printf("%04x\n", ethernet_type);

	switch (ethernet_type) 
	{
	case 0x0800:
		printf("网络层是：\tIPv4协议\n");break;
	case 0x0806:
		printf("网络层是：\tARP协议\n");break;
	case 0x8035:
		printf("网络层是：\tRARP 协议\n");break;
	default: break;
	}
	/*获得Mac源地址*/
	printf("Mac源地址:\t");
	mac_string = ethernet_protocol->ether_shost;
	printf("%02x:%02x:%02x:%02x:%02x:%02x:\n", *mac_string, *(mac_string + 1), *(mac_string + 2), *(mac_string + 3), *(mac_string + 4), *(mac_string + 5));

	/*获得Mac目的地址*/
	printf("Mac目的地址:\t");
	mac_string = ethernet_protocol->ether_dhost;
	printf("%02x:%02x:%02x:%02x:%02x:%02x:\n", *mac_string, *(mac_string + 1), *(mac_string + 2), *(mac_string + 3), *(mac_string + 4), *(mac_string + 5));

	switch (ethernet_type)
	{
	case 0x0800:
		ip_protocol_packet_callback(argument, packet_header, packet_content);
		break;
	case 0x0806:
		arp_protocol_packet_callback(argument, packet_header, packet_content);
		break;
	default:break;
	}

	printf("\n--------------------------------------------\n");
	packet_number++;
}

int main() {
	pcap_if_t *alldevs;
	pcap_if_t *adapter;
	pcap_t *adhandle;
	char errbuf[PCAP_ERRBUF_SIZE];

	/*获取本地机器设备列表*/
	if (pcap_findalldevs_ex(PCAP_SRC_IF_STRING, NULL, &alldevs, errbuf) == -1)
	{
		fprintf(stderr, "Error in pcap_findalldevs_ex:%s\n", errbuf);
		exit(1);
	}
	/*打印列表*/
	int i = 1;
	for (adapter = alldevs;adapter != NULL;adapter = adapter->next)
	{
		printf("%d. %s", i++, adapter->name);
		if (adapter->description)
			printf(" (%s)\n", adapter->description);
		else
			printf(" (No description available)\n");
	}

	if (i == 0)
	{
		printf("\n No interfaces found!\n");
		exit(1);
	}
	
	
	int n;
	printf("输入希望捕获的适配器号\n");
	scanf("%d", &n);
	for (adapter = alldevs, i = 0; i<n;adapter = adapter->next, i++);

	if((adhandle = pcap_open(adapter->name,          // 设备名
		65536,            // 65535保证能捕获到不同数据链路层上的每个数据包的全部内容
		PCAP_OPENFLAG_PROMISCUOUS,    // 混杂模式
		1000,             // 读取超时时间
		NULL,             // 远程机器验证
		errbuf            // 错误缓冲池
	)) == NULL)
	{
		fprintf(stderr, "\nUnable to open the adapter. %s is not supported by WinPcap\n", adapter->name);
		/* 释放设备列表 */
		pcap_freealldevs(alldevs);
		return -1;
	}

	printf("\nlistening on %s...\n", adapter->description);

	printf("输入希望捕获的数据包数量\n");
	scanf("%d", &n);

	pcap_loop(adhandle, n, ethernet_protocol_packet_callback, NULL);

	pcap_freealldevs(alldevs);


	return 0;
}




