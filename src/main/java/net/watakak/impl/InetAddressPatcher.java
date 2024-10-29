package net.watakak.impl;

import com.google.common.net.InetAddresses;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressPatcher
{
    @SuppressWarnings("UnstableApiUsage")
    public static InetAddress patch(String hostName, InetAddress addr) throws UnknownHostException
    {
        if (InetAddresses.isInetAddress(hostName))
        {
            InetAddress patched = InetAddress.getByAddress(addr.getHostAddress(), addr.getAddress());
            addr = patched;
        }
        return addr;
    }
}