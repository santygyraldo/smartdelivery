# Configuración de IntelliJ IDEA para Smart Delivery

## Problema
IntelliJ IDEA estaba usando Java 24, que causa conflictos con Lombok.

## Solución

### Opción 1: Usar el Script (Recomendado)
Ejecuta el archivo `open-idea.bat` para abrir IntelliJ IDEA con Java 23:
```bash
.\open-idea.bat
```

### Opción 2: Configurar Manualmente en IntelliJ IDEA

1. **Cierra IntelliJ IDEA completamente**

2. **Abre IntelliJ IDEA normalmente**

3. **Ve a File → Project Structure** (Ctrl + Alt + Shift + S)

4. **En la sección "Project":**
   - Haz clic en el dropdown de **"Project SDK"**
   - Selecciona **"Add SDK"** → **"Download JDK"**
   - Selecciona **Oracle OpenJDK 23**
   - O si ya tienes Java 23, selecciona **"C:\Program Files\Java\jdk-23"**

5. **En "Project language level":**
   - Selecciona **"23 - Pattern matching, record patterns"**

6. **Haz clic en "Apply"** y luego **"OK"**

7. **Reconstruye el proyecto:**
   - Ve a **Build → Rebuild Project**

### Opción 3: Usar Maven Wrapper desde Terminal
```bash
$env:JAVA_HOME = "C:\Program Files\Java\jdk-23"
.\mvnw.cmd spring-boot:run
```

## Verificación

Cuando ejecutes desde IntelliJ, deberías ver en los logs:
```
Starting SmartDeliveryApplication using Java 23.0.2
```

## Notas Importantes

- La variable de entorno `JAVA_HOME` ha sido establecida permanentemente a Java 23
- Cierra y reabre IntelliJ IDEA después de hacer cambios
- Si aún ves errores, cierra IntelliJ completamente y ejecuta `open-idea.bat`
