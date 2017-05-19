FROM dock0/arch:latest

ENV userName=user

LABEL maintainer "bertof@protonmail.com"

RUN pacman -Syu nodejs npm --noconfirm

RUN useradd -m -G storage,power,wheel,users ${userName} && \
chown -R ${userName}:users /home/${userName}

ADD Node_server /home/${userName}/PiTest
RUN chown -R ${userName}:users /home/${userName}/PiTest

USER ${userName}

WORKDIR /home/${userName}/PiTest

#RUN npm install

CMD ["/bin/bash"]