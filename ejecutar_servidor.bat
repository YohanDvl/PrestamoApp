@echo off
title Servidor IMC RMI - LipeRMI
echo Iniciando Servidor IMC RMI en puerto 9007...
java -cp "EjemploLipeRmiServidorImc\dist\EjemploLipeRmiServidorImc.jar;EjemploLipeRmiServidorImc\lib\lipermi-1.0.1.jar;EjemploLipeRmiServidorImc\lib\EjemploLipeRmiLibImc.jar" johnarrieta.seminario.imc.rmi.Principal
pause
