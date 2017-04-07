##Instalar chef en la máquina virtual que vayamos a usar

- [x] Lo primero de todo es crear un nombre de host para la máquina virtual que se va a usar, lo que se
consigue modificando el archivo /etc/hosts, para que quede como en [esta captura](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_2/ejercicio_1/capturas/ej1_1.png).

- [x] Tras esto, procedemos a conseguir la dirección de descarga del servidor chef que colocaremos en la máquina virtual, tal y como se ve en la siguiente [captura](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_2/ejercicio_1/capturas/ej1_2.png)

- [x] Utilizando wget, descargamos mediante ese enlace el servidor en la máquina virtual. [captura](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_2/ejercicio_1/capturas/ej1_3.png).

- [x] Ahora, procedemos a su instalación, utilizando la utilidad dpkg de ubuntu, tal y como puede verse [aquí](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_2/ejercicio_1/capturas/ej1_4.png).

- [x] Utilizando el comando: "sudo chef-server-ctl reconfigure", dejamos que chef se configure.[captura](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_2/ejercicio_1/capturas/ej1_5.png).

- [x] Habiendo descargado de la misma forma que el server anteriormente, el cliente chef, pasamos a instalarlo con dpkg en nuestra máquina local de la siguiente manera. [captura](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_2/ejercicio_1/capturas/ej1_6.png).

- [x] Aunque tanto el servidor como el cliente ya se encuentran instalados, es recomendable la instalación de chef-manage, una api gráfica que facilita el uso de chef. [captura del comando](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_2/ejercicio_1/capturas/ej1_7.png) [captura de finalización del proceso](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_2/ejercicio_1/capturas/ej1_8.png).

- [x] ¡Todo listo! [captura](https://github.com/manuelbr/ejercicios_CC/tree/master/tema_2/ejercicio_1/capturas/ej1_9.png) (En el momento de la toma de esta captura, había que reiniciar el servicio de chef y por ello aparece que no estaba instalada la consola de gestión de chef, aunque realmente lo estaba).
