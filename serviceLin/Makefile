# Makefile para instalar GtfsrtExpo
# Edgar Moncada
# Create 16-04-2015
# Ejecutar solo como root

COMMON_NAME=GtfsrtExpo

INSTALL_PATH=/usr/local/$(COMMON_NAME)
CONFIG_PATH=/usr/local/etc/$(COMMON_NAME)
DAEMON_NAME=dm$(COMMON_NAME)
SERVICE_PATH=/etc/init.d/$(DAEMON_NAME)
JAR_NAME=$(COMMON_NAME).jar
RES_PATH=resources
RUN_USER=$(USER)
$(eval IS_PRINT_ERROR := true)

#dependiendo de la distribucion almacena y configura el demonio
DAEMON_NAME_DISTRO=""
DAEMON_DEBIAN=$(DAEMON_NAME)-deb
DISTRO_DEBIAN=$(shell  cat /etc/*-release | grep -i -E "ubuntu" | head -1)
DAEMON_REDHAT=$(DAEMON_NAME)-epel
DISTRO_REDHAT=$(shell  cat /etc/*-release | grep -i -E "centos|redhat" | head -1)

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
	@if [ ! -z "$(DISTRO_DEBIAN)" ]; then cp $(DAEMON_DEBIAN) $(SERVICE_PATH); echo "cp $(DAEMON_DEBIAN) $(SERVICE_PATH)"; fi
	@if [ ! -z "$(DISTRO_REDHAT)" ]; then cp $(DAEMON_REDHAT) $(SERVICE_PATH); echo "cp $(DAEMON_REDHAT) $(SERVICE_PATH)"; fi
	chmod +x $(INSTALL_PATH)/$(DAEMON_NAME).sh;
	chmod 755 $(SERVICE_PATH);
	ln -s $(INSTALL_PATH)/$(DAEMON_NAME).sh /usr/bin/$(DAEMON_NAME).sh;
	@echo ""
	@echo "Instalado"
	@echo ""
endef

define blockRundaemon
	@if [ -f "$(SERVICE_PATH)" ] && [ ! -z "$(DISTRO_DEBIAN)" ]; then update-rc.d $(DAEMON_NAME) defaults; echo "update-rc.d $(DAEMON_NAME) defaults" ; fi
	@if [ -f "$(SERVICE_PATH)" ] && [ ! -z "$(DISTRO_REDHAT)" ]; then chkconfig --add $(DAEMON_NAME); echo "chkconfig --add $(DAEMON_NAME)"; chkconfig $(DAEMON_NAME) on; echo "chkconfig $(DAEMON_NAME) on"; fi
	@if [ -f "$(SERVICE_PATH)" ]; then $(SERVICE_PATH) start; echo "$(SERVICE_PATH) start"; else echo "No existe $(SERVICE_PATH)"; fi
	@if [ -f "$(SERVICE_PATH)" ]; then echo ""; echo "Autoarranque e iniciando el demonio $(DAEMON_NAME)."; echo ""; fi
endef


define blockRemove
	@echo ""
	@echo "Removiendo ..."
	@echo ""
	@echo "deteniendo servicio ..."
	@if [ -f $(SERVICE_PATH) ]; then $(SERVICE_PATH) stop; echo "$(SERVICE_PATH) stop"; fi
	@echo "detenido ..."
	@if [ -f $(SERVICE_PATH) ] && [ ! -z "$(DISTRO_REDHAT)" ]; then chkconfig --del $(DAEMON_NAME); echo "chkconfig --del $(DAEMON_NAME)"; fi
	rm -rf $(INSTALL_PATH) $(SERVICE_PATH) /usr/bin/$(DAEMON_NAME).sh
	@if [ -f $(SERVICE_PATH) ] && [ ! -z "$(DISTRO_DEBIAN)" ]; then update-rc.d $(DAEMON_NAME) remove; echo "update-rc.d $(DAEMON_NAME) remove"; fi 
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


