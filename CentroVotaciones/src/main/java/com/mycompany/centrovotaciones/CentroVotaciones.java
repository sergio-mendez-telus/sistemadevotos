/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.centrovotaciones;


import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


/**
 *
 * @author Crist
 */

public class CentroVotaciones {

public enum Rol {
        ADMINISTRADOR("Administrador"),
        REGISTRADOR_DE_VOTANTES("Registrador de votantes"),
        AUDITOR("Auditor"),
        VOTANTE("Votante");

        private final String nombre;

        Rol(String nombre) {
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }

}
    @Getter
    @Setter
    @NoArgsConstructor
    static
    class Usuario{
        public static Set<Usuario> usuarios = new HashSet<>();
        private int id;
        private String nombre;
        private String apellido;
        private String correo;
        private String contrasenia;
        private boolean activo;
        private List<String> roles = new ArrayList<>();
        public Usuario(int id, String nombre, String apellido, String correo, String contrasenia, List<String> roles, boolean activo) {
            this.id = id;
            this.nombre = nombre;
            this.apellido = apellido;
            this.correo = correo;
            this.contrasenia = contrasenia;
            this.roles = roles;
            this.activo = activo;
        }

        public String toCSVFormat() {
            StringBuilder csvFormat = new StringBuilder();
            csvFormat.append("\"").append(id).append("\",");
            csvFormat.append("\"").append(nombre).append("\",");
            csvFormat.append("\"").append(apellido).append("\",");
            csvFormat.append("\"").append(correo).append("\",");
            csvFormat.append("\"").append(contrasenia).append("\",");
            csvFormat.append("\"").append(activo).append("\",");

            // Formatear la lista de roles
            StringBuilder rolesCSV = new StringBuilder();
            for (String role : roles) {
                rolesCSV.append(role).append(",");
            }
            if (rolesCSV.length() > 0) {
                rolesCSV.setLength(rolesCSV.length() - 1); // Eliminar la última coma
            }
            csvFormat.append("\"").append(rolesCSV).append("\"");

            return csvFormat.toString();
        }

        public String getRoles(){
            String rolesString = "";
            for (String role : roles) {
                rolesString += role + ",";
            }
            return rolesString;
        }


        public Usuario crearUsuario(){
            List<String> rolesSeleccionados = new ArrayList<>();
            leer.nextLine();
            System.out.println("Ingrese el nombre del usuario");
            String nombre = leer.nextLine();
            System.out.println("Ingrese el apellido del usuario");
            String apellido = leer.nextLine();
            System.out.println("Ingrese el correo del usuario");
            String correo = leer.nextLine();
//            System.out.println("Ingrese la contraseña del usuario");
            String contrasenia = generarContrasenaAleatoria(16);
            String respuesta = "";
            do {
                System.out.println("1. Administrador");
                System.out.println("2. Registradores de votantes");
                System.out.println("3. Auditor");
                System.out.println("Ingrese el rol del usuario");
                int  opcion = leer.nextInt();

                switch (opcion){
                    case 1:
                        rolesSeleccionados.add(Rol.ADMINISTRADOR.getNombre());
                        break;
                    case 2:
                        rolesSeleccionados.add(Rol.REGISTRADOR_DE_VOTANTES.getNombre());
                        break;
                    case 3:
                        rolesSeleccionados.add(Rol.AUDITOR.getNombre());
                        break;
                    default:
                        System.out.println("Opcion no valida");
                        break;
                }
                leer.nextLine();
                System.out.println("Desea agregar otro rol? S/N");
                respuesta = leer.nextLine();
            } while (respuesta.equalsIgnoreCase("S"));

            Usuario usuario = new Usuario();
            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            usuario.setCorreo(correo);
            usuario.setContrasenia(contrasenia);
            usuario.setActivo(true);
            usuario.setRoles(rolesSeleccionados);
            return usuario;
        }

        public Usuario modificarUsuario(Usuario usuario){
            leer.nextLine();
            System.out.println("Ingrese el nombre del usuario");
            String nombre = leer.nextLine();
            System.out.println("Ingrese el apellido del usuario");
            String apellido = leer.nextLine();

            usuario.setNombre(nombre);
            usuario.setApellido(apellido);
            return usuario;
        }

        public void reiniciarContrasenia(Usuario usuario){
            leer.nextLine();
            System.out.println("Ingrese la nueva contraseña");
            String contrasenia = leer.nextLine();
            usuario.setContrasenia(contrasenia);
        }

        public void inactivarUsuario(Usuario usuario){
            usuario.setActivo(false);
        }
    }
    static String adminContrasenia = "admin";
    static  Scanner leer = new Scanner(System.in);
    public static void main(String[] args) {
        Usuario.usuarios = cargarUsuariosDesdeCSV("dbaUsuarios.csv");
        System.out.println(Usuario.usuarios.size());
        iniciarApp();
    }

     public static void iniciarApp(){
        System.out.println("Sistema de votaciones");
        System.out.println("Ingrese una contraseña para el administrador");
        String contraseniaIngresada = leer.nextLine();
        if (contraseniaIngresada.equals(adminContrasenia)){
            System.out.println("Bienvenido administrador");
            menuRegistroDeUsuarios();
        }else{
            System.out.println("Contraseña incorrecta");
        }
     }
    public static void menuRegistroDeUsuarios(){

        System.out.println("1. Crear usuario del sistema");
        System.out.println("2. Modificar usuario del sistema");
        System.out.println("3. Reiniciar contraseña");

        int opcion = leer.nextInt();
        switch (opcion){
            case 1:
                Usuario usuarioNuevo = new Usuario();
                usuarioNuevo =usuarioNuevo.crearUsuario();
                guardarUsuario(usuarioNuevo);
                break;
            case 2:
                leer.nextLine();
                System.out.println("Ingrese el correo del usuario");
                String correo = leer.nextLine();
                Usuario.usuarios.forEach(usuario1 -> {
                    if (usuario1.getCorreo().equals(correo)){
                        usuario1.modificarUsuario(usuario1);
                        ejecurActualizacion();
                    }else{
                        System.out.println("Usuario no encontrado");
                    }
                });
                break;
            case 3:
                leer.nextLine();
                System.out.println("Ingrese el correo del usuario");
                String correo1 = leer.nextLine();
                Usuario.usuarios.forEach(usuario1 -> {
                    if (usuario1.getCorreo().equalsIgnoreCase(correo1)){
                        usuario1.reiniciarContrasenia(usuario1);
                        ejecurActualizacion();
                    }else{
                        System.out.println("Usuario no encontrado");
                    }
                });
                break;

            default:
                System.out.println("Opcion no valida");
                break;
        }
    }

    public static Set<Usuario> cargarUsuariosDesdeCSV(String csvFilePath) {
        Set<Usuario> usuarios = new HashSet<>();

        try {
            FileReader fileReader = new FileReader(csvFilePath);
            CSVReader reader = new CSVReader(fileReader);

            String[] nextLine;

            boolean firstLine = true;


            while ((nextLine = reader.readNext()) != null) {
                if (firstLine) {
                    firstLine = false; // Omitir la primera línea
                    continue;
                }

                if (nextLine.length != 7) {
                    System.err.println("Error: formato de línea incorrecto.");
                    continue;
                }

                Usuario usuario = new Usuario();
                usuario.setId(Integer.parseInt(nextLine[0]));
                usuario.setNombre(nextLine[1]);
                usuario.setApellido(nextLine[2]);
                usuario.setCorreo(nextLine[3]);
                usuario.setContrasenia(nextLine[4]);
                usuario.setActivo(Boolean.parseBoolean(nextLine[5]));
                String[] rolesArray = nextLine[6].split(",");
                List<String> rolesList = Arrays.asList(rolesArray);
                usuario.setRoles(rolesList);
                usuarios.add(usuario);
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
//        return usuarios.stream().filter(votante -> !votante.isActivo())
//                .collect(Collectors.toSet());

                return usuarios.stream().filter(Usuario::isActivo)
                .collect(Collectors.toSet());
    }
    public static void guardarUsuario(Usuario usuario) {
        String csvFile = "dbaUsuarios.csv";

        try {
            File file = new File(csvFile);
            // Verifica si el archivo ya existe
            boolean existeArchivo = file.exists();

            // Abre el archivo en modo anexar
            CSVWriter writer = new CSVWriter(new FileWriter(file, true));

            // Si el archivo no existía previamente, escribe el encabezado
            if (!existeArchivo) {
                String[] header = {"id", "nombre", "apellido", "correo", "contrasenia", "activo", "roles"};
                writer.writeNext(header);
            }

            String id = Usuario.usuarios.size() + 1 + "";
            String nombre = (usuario.getNombre() != null) ? usuario.getNombre() : "";
            String apellido = (usuario.getApellido() != null) ? usuario.getApellido() : "";
            String correo = (usuario.getCorreo() != null) ? usuario.getCorreo() : "";
            String contrasenia = (usuario.getContrasenia() != null) ? usuario.getContrasenia() : "";
            String activo = String.valueOf(usuario.isActivo());
            String roles = (usuario.getRoles() != null) ? usuario.getRoles() : "";

            String[] data = {id, nombre, apellido, correo, contrasenia, activo, roles};
            writer.writeNext(data);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ejecurActualizacion(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("dbaUsuarios.csv"));
            writer.write("id;nombre;apellido;correo;contrasenia;activo;roles");
            writer.newLine();
            for (Usuario usuario : Usuario.usuarios) {
                writer.write(usuario.toCSVFormat());
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generarContrasenaAleatoria(int longitud) {
        StringBuilder contrasena = new StringBuilder();
        String CARACTERES_VALIDOS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        Random random = new Random();

        for (int i = 0; i < longitud; i++) {
            int indice = random.nextInt(CARACTERES_VALIDOS.length());
            contrasena.append(CARACTERES_VALIDOS.charAt(indice));
        }

        return contrasena.toString();
    }

}

