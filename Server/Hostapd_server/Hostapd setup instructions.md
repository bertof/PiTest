# Hostapd setup instructions  
## Instructions to setup a hostapd service and mae it generate a wifi hotspot  
All the following instructions are valid on [Arch Linux](https://www.archlinux.org/) (May 2017).  
The interface that will generate the hotspot is called `wlan1`.  
1. Update your system  
    ```shell  
    $ sudo pacman -Syu  
    ```  
2. Install needed packages  
    - `hostapd`  
    - `dnsmasq`  
    ```shell  
    $ sudo pacman -S hostapd dnsmasq  
    ```  
3. Set static IP for wlan1  
    Edit `/etc/dhcpcd.conf` and add the following instructions:  
    ```  
    interface wlan1  
    static ip_address=192.168.0.10/24  
    static routers=192.168.0.1  
    static domain_name_servers=192.168.0.1 8.8.8.8 8.8.4.4  
    ```  
4. Configure `hostapd` creating a new config file `/etc/hostapd/hostapd.conf` with the following text:  
    ```  
    # This is the name of the WiFi interface we configured above  
    interface=wlan1  
    # Use the nl80211 driver with the brcmfmac driver  
    driver=nl80211  
    # This is the name of the network  
    ssid=PiTest  
    # Use the 2.4GHz band  
    hw_mode=g  
    # Use channel 6  
    channel=6  
    # Enable 802.11n  
    ieee80211n=1  
    # Enable WMM  
    wmm_enabled=1  
    # Enable 40MHz channels with 20ns guard interval  
    ht_capab=[HT40][SHORT-GI-20][DSSS_CCK-40]  
    # Accept all MAC addresses  
    macaddr_acl=0  
    # Use WPA authentication  
    auth_algs=1  
    # Require clients to know the network name  
    ignore_broadcast_ssid=0  
    # Use WPA2  
    wpa=2  
    # Use a pre-shared key  
    wpa_key_mgmt=WPA-PSK  
    # The network passphrase  
    wpa_passphrase=SuperSecretPassword  
    # Use AES, instead of TKIP  
    rsn_pairwise=CCMP  
    ```  
    If you run  
    ```shell  
    $ sudo hostapd /etc/hostapd/hostapd.conf  
    ```  
    a WiFi network should come up. You can authenticate to it but you can't get an IP automatically yet.  
5. Modify the `hostapd` default config to include the file we have just created: at `/etc/default/hostapd` change:  
    ```  
    #DAEMON_CONF=""  
    ```  
    to  
    ```  
    DAEMON_CONF="/etc/hostapd/hostapd.conf"  
    ```  
6. Configure `dnsmasq` setting `/etc/dnsmasq.conf` as follows:  
    ```  
    # Use interface wlan1  
    interface=wlan1  
    # Explicitly specify the address to listen on  
    listen-address=172.24.1.1  
    # Bind to the interface to make sure we aren't sending things elsewhere  
    bind-interfaces  
    # Forward DNS requests to Google DNS  
    server=8.8.8.8  
    # Don't forward short names  
    domain-needed  
    # Never forward addresses in the non-routed address spaces.  
    bogus-priv  
    # Assign IP addresses between 172.24.1.50 and 172.24.1.150 with a 12 hour lease time  
    dhcp-range=172.24.1.50,172.24.1.150,12h  
    ```

7. Setup IP forwarding
    Create the file `/etc/sysctl.d/30-ipforward.conf` and add the following text:  
    ```
    net.ipv4.ip_forward=1
    net.ipv6.conf.default.forwarding=1
    net.ipv6.conf.all.forwarding=1
    ```

    This will enable IP packets forwarding since the next reboot, but we want it now, so use the following command:
    ```shell
    $ sudo sh -c "echo 1 > /proc/sys/net/ipv4/ip_forward"
    ```

8. Enable internet nat
    Use the following commands to allow postrouting of the packages and routing them between `eth0` and `wlan1`.
    ```
    sudo iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE  
    sudo iptables -A FORWARD -i eth0 -o wlan1 -m state --state RELATED,ESTABLISHED -j ACCEPT  
    sudo iptables -A FORWARD -i wlan1 -o eth0 -j ACCEPT
    ```









## Credits  
The configuration guide is adapted from [Phil Martin's work](https://frillip.com/using-your-raspberry-pi-3-as-a-wifi-access-point-with-hostapd/).  
The routing procedure is directly taken from the [Arch Wiki](https://wiki.archlinux.org/index.php/Internet_sharing).