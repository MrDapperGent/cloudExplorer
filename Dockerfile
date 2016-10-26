FROM ubuntu
RUN apt-get update;
RUN apt-get install -y software-properties-common
RUN add-apt-repository ppa:openjdk-r/ppa
RUN apt-get update;apt-get -y install fvwm xterm tightvncserver openjdk-8-jdk xfonts-base

#Configure Startup Script
RUN echo "#!/bin/bash" > /opt/start.sh
RUN echo "rm -rf /tmp/.X*" >> /opt/start.sh
RUN echo "vncserver" >> /opt/start.sh
RUN echo "sleep infinity" >> /opt/start.sh
RUN chmod +x /opt/start.sh

#Add main package and VNC information
ADD dist /
RUN mkdir /root/.vnc
RUN echo 123456 | vncpasswd -f > /root/.vnc/passwd
RUN echo "fvwm &" > /root/.vnc/xstartup
RUN echo "exec java -jar /CloudExplorer.jar" >> /root/.vnc/xstartup

RUN chmod 600 /root/.vnc/passwd
RUN chmod 777 /root/.vnc/xstartup
ENV USER root
CMD /opt/start.sh
