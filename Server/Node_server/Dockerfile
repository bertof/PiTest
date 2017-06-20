FROM dock0/arch:latest
 
ENV USERNAME=user 
ENV OPEN_PORT=4646
 
EXPOSE ${OPEN_PORT}

LABEL maintainer "bertof@protonmail.com" 
 
# Install nodejs and npm 
RUN pacman -Syu nodejs npm --noconfirm

# Install mocha globally
RUN npm install -g mocha nodemon
 
# Add user 
RUN useradd -m -G storage,power,wheel,users ${USERNAME} && \ 
chown -R ${USERNAME}:users /home/${USERNAME} 
 
# Copy app files and set permissions 
ADD . /home/${USERNAME}/PiTest 
RUN chown -R ${USERNAME}:users /home/${USERNAME}/PiTest 
 
# Switch to user in app directory 
USER ${USERNAME} 
WORKDIR /home/${USERNAME}/PiTest 
 
# Install dependencies 
RUN npm install

ENTRYPOINT ["npm", "start"]