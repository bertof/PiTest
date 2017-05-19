FROM dock0/arch:latest
ENTRYPOINT ["npm", "test"]

ENV userName=user

LABEL maintainer "bertof@protonmail.com"

# Install nodejs and npm
RUN pacman -Syu nodejs npm --noconfirm

# Install mocha globally
RUN npm install -g mocha

# Add user
RUN useradd -m -G storage,power,wheel,users ${userName} && \
chown -R ${userName}:users /home/${userName}

# Copy app files and set permissions
ADD Node_server /home/${userName}/PiTest
RUN chown -R ${userName}:users /home/${userName}/PiTest

# Switch to user in app directory
USER ${userName}
WORKDIR /home/${userName}/PiTest

# Install dependencies
RUN npm install