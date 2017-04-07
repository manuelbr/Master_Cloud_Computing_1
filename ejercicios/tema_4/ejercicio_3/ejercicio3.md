##Ejercicio 3: Provisionar un contenedor LXC usando Ansible o alguna otra herramienta de configuración que ya se haya usado

Para ello, lo único que tenemos que hacer es cambiar el "target" del script de provisionamiento (de ansible en mi caso), estableciendo el nombre del grupo de hosts destino que necesitamos, quedando algo así:

```
---
- hosts: ubuntuLXC
  become: yes
  remote_user: ubuntu

  tasks:
  - name: Instalar Apache2
    apt: 
      update_cache: yes
      name: apache2 
      state: present
```

Previamente a ésto, debemos definir el host establecido en el script, en el dichero hosts que se encuentra en el directorio de instalación de ansible, en la ruta (en ubuntu): /etc/ansible/, en el que colocamos la ip del host a definir con la siguiente sintaxis:

```
[ubuntuLXC]
host1 ansible_ssh_host=ip
```

La ip que debemos introducir la podemos obtener de la máquina virtual de lxc que tiene cargada la distribución a provisionar, con el comando: "sudo lxc-ls -f". Ejecutamos a continuación el script de ansible con el comando del tipo:

```
ansible-playbook -i (Directorio donde está el fichero hosts: /etc/ansible/hosts) (Directorio donde se encuentre el script.yml) --ask-pass (porque no usaremos clave privada) --ask-sudo-pass (para dar factor de seguridad al tener que introducir la clave de root)
```


