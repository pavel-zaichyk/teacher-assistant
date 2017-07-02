#!/usr/bin/env bash

# usable both in Linux and MacOS systems

os_name=$(uname -a)

if [[ $os_name == *"Linux"* ]]; then
  echo "export TOMCAT_DIR="$PWD >> ~/.bashrc
else
  echo "export TOMCAT_DIR="$PWD >> ~/.bash_profile
fi

echo "System variable TOMCAT_DIR set to: "$PWD
echo "Restart is needed to apply changes."
