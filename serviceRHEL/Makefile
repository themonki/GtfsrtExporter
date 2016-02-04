# Makefile para instalar dmGtfsrtExpo para systemd
# Edgar Moncada
# Create 05-01-2016
# Ejecutar solo como root

COMMON_NAME=GtfsrtExpo

INSTALL_PATH=/usr/local/$(COMMON_NAME)
CONFIG_PATH=/usr/local/etc/$(COMMON_NAME)
DAEMON_NAME=dm$(COMMON_NAME)
SERVICE_PATH=/etc/systemd/system/$(DAEMON_NAME).service
JAR_NAME=$(COMMON_NAME).jar
RES_PATH=resources
RUN_USER=$(USER)
$(eval IS_PRINT_ERROR := true)

#dependiendo de la distribucion almacena y configura el demonio
DAEMON_REDHAT=$(DAEMON_NAME).service
DISTRO_REDHAT=$(shell  cat /etc/*-release | grep -i -E "centos|red hat" | head -1)

all: install

install: remove
ifeq "root"  "$(RUN_USER)"
	$(call blockInstall)
else
	$(call printerror)
endif

remove:
ifeq "root"  "$(RUN_USER)"
	$(call blockRemove)
else
	$(call printerror)
endif

purge: remove
ifeq "root"  "$(RUN_USER)"
	$(call blockPurge)
else
	$(call printerror)
endif

rundaemon:
ifeq "root"  "$(RUN_USER)"
	$(call blockRundaemon)
else
	$(call printerror)
endif

installandrun: install rundaemon

define printerror
	@if $(IS_PRINT_ERROR); then echo "No es usuario root, saliendo."; fi
	$(eval IS_PRINT_ERROR := false)
endef

define blockInstall
	@echo ""
	@echo "Instalando ..."
	@echo ""
	mkdir -p $(INSTALL_PATH);
	cp -R $(DAEMON_NAME).sh usage.txt $(JAR_NAME) $(INSTALL_PATH);
	@if [ ! -z "$(DISTRO_REDHAT)" ]; then cp $(DAEMON_REDHAT) $(SERVICE_PATH); echo "cp $(DAEMON_REDHAT) $(SERVICE_PATH)"; fi
	chmod +x $(INSTALL_PATH)/$(DAEMON_NAME).sh;
#	chmod 755 $(SERVICE_PATH);
	ln -s $(INSTALL_PATH)/$(DAEMON_NAME).sh /usr/bin/$(DAEMON_NAME).sh;
	@if [ ! -d $(CONFIG_PATH) ]; then mkdir -p $(CONFIG_PATH); echo "mkdir -p $(CONFIG_PATH)"; cp -R $(RES_PATH) $(CONFIG_PATH); echo "cp -R $(RES_PATH) $(CONFIG_PATH)";  else echo "Existe el directorio $(CONFIG_PATH)";fi
	@if [ -f "$(CONFIG_PATH)/$(RES_PATH)/config.properties" ]; then  echo "Existe archivo de configuracion $(CONFIG_PATH)/$(RES_PATH)/config.properties. No se reemplaza"; else if [ ! -f $(RES_PATH)/config.properties ]; then echo "Warning: no existe archivo $(CONFIG_PATH)/$(RES_PATH)/config.properties."; fi ; fi
	@if [ ! -f "$(CONFIG_PATH)/$(RES_PATH)/config.properties" ] && [ -f $(RES_PATH)/config.properties ]; then cp $(RES_PATH)/config.properties $(CONFIG_PATH)/$(RES_PATH); echo "cp $(RES_PATH)/config.properties $(CONFIG_PATH)/$(RES_PATH)"; fi;
	@echo ""
	@echo "Instalado"
	@echo "Revisar que exista el archivo $(CONFIG_PATH)/$(RES_PATH)/config.properties y este configurado adecuadamente."
	@echo "El log se almacenara en $(INSTALL_PATH)"
	@echo ""
endef

define blockRundaemon
	@if [ -f "$(SERVICE_PATH)" ] && [ ! -z "$(DISTRO_REDHAT)" ]; then systemctl daemon-reload; echo "systemctl daemon-reload"; systemctl start $(DAEMON_REDHAT); echo "systemctl start $(DAEMON_REDHAT)"; systemctl enable $(DAEMON_REDHAT); echo "systemctl enable $(DAEMON_REDHAT)"; fi
	@if [ -f "$(SERVICE_PATH)" ] && [ ! -z "$(DISTRO_REDHAT)" ]; then echo ""; echo "Autoarranque e iniciando el demonio $(DAEMON_NAME)."; echo ""; fi
endef


define blockRemove
	@echo ""
	@echo "Removiendo ..."
	@echo ""
	@echo "deteniendo servicio ..."
	@if [ -f $(SERVICE_PATH) ] && [ ! -z "$(DISTRO_REDHAT)" ]; then systemctl stop $(DAEMON_REDHAT); echo "systemctl stop $(DAEMON_REDHAT)"; fi
	@echo "detenido ..."
	@if [ -f $(SERVICE_PATH) ] && [ ! -z "$(DISTRO_REDHAT)" ]; then systemctl disable $(DAEMON_REDHAT); echo "systemctl disable $(DAEMON_REDHAT)"; fi
	rm -rf $(INSTALL_PATH) $(SERVICE_PATH) /usr/bin/$(DAEMON_NAME).sh
	@echo ""
	@echo "Removido el componente en $(INSTALL_PATH) y $(SERVICE_PATH)."
	@echo ""
endef

define blockPurge
	@echo ""
	@echo "Purgando ..."
	@echo ""
	rm -rf $(CONFIG_PATH)
	@echo ""
	@echo "Eliminados archivos de configuracion en  $(CONFIG_PATH)."
	@echo ""
endef


