Directions to run assignment 2 program:


1. Copy program folder to linux lab computer with WinSCP
2. Open two instances of putty and connect to the lab01.cs.ndsu.nodak.edu
3. In one putty window drill into ~/public_html/proxy/src$ 
4. Compile one class with command: javac proxy/ProxyServer.java
5. Compile one class with command: javac proxy/ProxyThread.java
6. Run command: java proxy.ProxyServer
7. Should print out "Started on: 10000", which means the proxy is running on port 10000
8. Go to the other instance of putty
9. Type the command: telnet 127.0.0.1 10000
10. After you are connected type: GET http://www.cs.ndsu.nodak.edu/aboutus.htm HTTP/1.0
11. Other putty window will state "Request for : http://www.cs.ndsu.nodak.edu/aboutus.htm"
12. Go to the folder location in the lab using WinSCP
13. You will find a text file called proxy.log
14. Open it and you can veiw the recorded information about the connection that was just made
