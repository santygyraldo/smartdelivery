# Configuración de Java 23 para Smart Delivery

## Problema
El proyecto requiere Java 23 para compilar correctamente. Java 24 causa incompatibilidades con Lombok.

## Solución Aplicada

Se han configurado los siguientes archivos para forzar el uso de Java 23:

### 1. `.mvn/maven.config`
Configura Maven para usar Java 23 en todas las compilaciones.

### 2. `.mvn/jvm.config`
Configura la JVM para Maven.

### 3. `pom.xml`
Actualizado con:
- `java.version=23`
- `lombok.version=1.18.32` (compatible con Java 23)
- Configuración explícita del compilador con ruta a `javac.exe` de Java 23

### 4. Scripts de ejecución
- `run.bat` - Para ejecutar desde CMD
- `run.ps1` - Para ejecutar desde PowerShell

## Cómo Ejecutar

### Opción 1: Desde IntelliJ IDEA
1. Abre el proyecto en IntelliJ IDEA
2. Ve a `Run → Edit Configurations`
3. Crea una nueva configuración de tipo "Maven"
4. En "Command line", escribe: `spring-boot:run`
5. Haz clic en "Run"

### Opción 2: Desde Terminal (CMD)
```bash
run.bat
```

### Opción 3: Desde PowerShell
```powershell
.\run.ps1
```

### Opción 4: Usando Maven Wrapper
```bash
mvnw.cmd spring-boot:run
```

## Verificación

Para verificar que se está usando Java 23, busca en los logs:
```
Starting SmartDeliveryApplication using Java 23.0.2
```

## Notas
- Java 23 debe estar instalado en: `C:\Program Files\Java\jdk-23`
- Si instalaste Java 23 en otra ubicación, actualiza la ruta en:
  - `pom.xml` (línea 152)
  - `run.bat`
  - `run.ps1`
