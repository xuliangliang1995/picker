# `Docker` 部署 `Ftp Server`

![](https://picker-oss.oss-cn-beijing.aliyuncs.com/20191111/f4aa5f2db08c0b425cefe3a38e33dea4.jpg_target)

之前在研究一个开源项目的时候，需要搭建一个 `Ftp Server` 服务器。记录一下

## `docker-compose.yml`
* 用户名：`hc`
* 密码：`hc_2020_test`
```yml
version: "3.7"
services:
  ftp_server:
    image: fauria/vsftpd
    container_name: ftp
    ports:
      - "20:20"
      - "21:21"
      - "21100-21110:21100-21110"
    environment:
      FTP_USER: hc
      FTP_PASS: hc_2020_test
      PASV_ADDRESS: 62.234.145.128
      PASV_MIN_PORT: 21100
      PASV_MAX_PORT: 21110
    volumes:
      - /home/xuliang/hc/:/home/vsftpd/hc/
      - /etc/localtime:/etc/localtime
    restart: always

```