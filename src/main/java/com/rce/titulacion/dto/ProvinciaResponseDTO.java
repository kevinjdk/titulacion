package com.rce.titulacion.dto;

public class ProvinciaResponseDTO {
    private Long id;
    private String nombre;
    private RegionSummaryDTO region;

    public ProvinciaResponseDTO() {}

    public ProvinciaResponseDTO(Long id, String nombre, RegionSummaryDTO region) {
        this.id = id;
        this.nombre = nombre;
        this.region = region;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public RegionSummaryDTO getRegion() {
        return region;
    }

    public void setRegion(RegionSummaryDTO region) {
        this.region = region;
    }

    public static class RegionSummaryDTO {
        private Long id;
        private String nombre;

        public RegionSummaryDTO() {}

        public RegionSummaryDTO(Long id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
    }
}
