package com.multidisciplinar.docsecurepro.application.service.api;

import com.multidisciplinar.docsecurepro.api.dao.Archivo;
import com.multidisciplinar.docsecurepro.api.dao.Log;
import com.multidisciplinar.docsecurepro.application.service.database.ArchivosService;
import com.multidisciplinar.docsecurepro.application.service.database.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FtpService {

    private ArchivosService archivosService;
    private LogService logService;

    @Autowired
    public FtpService(ArchivosService archivosService, LogService logService) {
        this.archivosService = archivosService;
        this.logService = logService;
    }

    public int recordFileUpload(Archivo archivo) {
        return this.archivosService.insert(archivo);
    }

    public Archivo findByRuta(String rutaArchivo) {
        return this.archivosService.findByRuta(fixRoute(rutaArchivo));

    }

    public int deleteByRuta(String rutaArchivo) {
        return this.archivosService.deleteByExactRuta(fixRoute(rutaArchivo));
    }

    public int deleteByContaingRuta(String rutaArchivo) {
        return this.archivosService.deleteByContainingRuta(fixRoute(rutaArchivo));
    }

    public int recordMovement(Log log) {
        return this.logService.insert(log);
    }

    private String fixRoute(String route) {
        String fixedRoute = route.replace("//", "/");
        if (route.length() > 1 && route.lastIndexOf("/") == route.length() - 1) {
            return fixedRoute.substring(0, route.length() - 1);
        } else {
            return fixedRoute;
        }
    }

}
