package Fruteria;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Fruteria {

    private String nombre;
    private ArrayList<Cliente> listaClientesNoAtendidos;
    private ArrayList<Cliente> listaClientesAtendidos;
    private int contadorClientes;

    public Fruteria(String nombre) {
        this.setFruteria(nombre);
        this.listaClientesAtendidos = new ArrayList<>();
        this.listaClientesNoAtendidos = new ArrayList<>();
        this.setContadorClientes(0);
    }

    public String getFruteria() {
        return this.nombre;
    }

    public void setFruteria(String fruteria) {
        this.nombre = fruteria;
    }

    public ArrayList<Cliente> getListaClientesNoAtendidos() {
        return this.listaClientesNoAtendidos;
    }

    public void setListaClientesNoAtendidos(ArrayList<Cliente> listaClientesNoAtendidos) {
        this.listaClientesNoAtendidos = listaClientesNoAtendidos;
    }

    public ArrayList<Cliente> getListaClientesAtendidos() {
        return this.listaClientesAtendidos;
    }

    public void setListaClientesAtendidos(ArrayList<Cliente> listaClientesAtendidos) {
        this.listaClientesAtendidos = listaClientesAtendidos;
    }

    public int getContadorClientes() {
        return this.contadorClientes;
    }

    public void setContadorClientes(int contadorClientes) {
        this.contadorClientes = contadorClientes;
    }

    public void nuevoCliente(Edad edad) {
        Cliente clienteAñadir = new Cliente(contadorClientes + 1, edad);
        this.listaClientesNoAtendidos.add(clienteAñadir);
        this.insertarEnTablaNoAtendidos(clienteAñadir);
        this.contadorClientes++;
    }
    //metodo para insertar en bbdd clientes que se añaden

    public boolean insertarEnTablaNoAtendidos(Cliente cliente) {
        try {
            Connection miConexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "developer", "developer");
            Statement miStatement = miConexion.createStatement();

            if (cliente.getEdad() == Edad.MAYOR) {
                String instruccionSql = "INSERT INTO CLIENTESNOATENDIDOS VALUES(" + cliente.getnTicket() + ",'Mayor')";
                miStatement.executeUpdate(instruccionSql);
            } else {
                String instruccionSql = "INSERT INTO CLIENTESNOATENDIDOS VALUES(" + cliente.getnTicket() + ",'Joven')";
                miStatement.executeUpdate(instruccionSql);
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Ha ocurrido un error al insertar el cliente en la base de datos");
            System.out.println(e.getMessage());
            return false;
        }

    }

    private int posicionMayores() {
        int posicion = -1;
        int i = 0;
        while (i < this.listaClientesNoAtendidos.size() && this.listaClientesNoAtendidos.get(i).getEdad() != Edad.MAYOR) {
            i++;
        }
        if (i < this.listaClientesNoAtendidos.size()) {
            posicion = i;
        }
        return posicion;
    }
//metodo para insertar en bbdd clientes que se atienden

    public boolean insertarEnTablaAtendidos(Cliente cliente) {
        try {
            Connection miConexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "developer", "developer");
            Statement miStatement = miConexion.createStatement();

            if (cliente.getEdad() == Edad.MAYOR) {
                String instruccionSql = "INSERT INTO CLIENTESATENDIDOS VALUES(" + cliente.getnTicket() + ",'Mayor')";
                miStatement.executeUpdate(instruccionSql);
            } else {
                String instruccionSql = "INSERT INTO CLIENTESATENDIDOS VALUES(" + cliente.getnTicket() + ",'Joven')";
                miStatement.executeUpdate(instruccionSql);
            }
            return true;
        } catch (SQLException e) {
            System.out.println("Ha ocurrido un error al insertar el cliente en la base de datos");
            System.out.println(e.getMessage());
            return false;
        }

    }

    public boolean atenderCliente() {

        if (this.listaClientesNoAtendidos.isEmpty()) {
            return false;
        } else {
            int posicionMayores = this.posicionMayores();
            if (posicionMayores >= 0) {
                this.listaClientesAtendidos.add(this.listaClientesNoAtendidos.get(posicionMayores));
                this.insertarEnTablaAtendidos(this.listaClientesNoAtendidos.get(posicionMayores));
                this.borrarCliente(this.listaClientesNoAtendidos.get(posicionMayores));
                this.listaClientesNoAtendidos.remove(posicionMayores);
                return true;
            } else {
                this.listaClientesAtendidos.add(this.listaClientesNoAtendidos.get(0));
                this.insertarEnTablaAtendidos(this.listaClientesNoAtendidos.get(0));
                 this.borrarCliente(this.listaClientesNoAtendidos.get(0));
                this.listaClientesNoAtendidos.remove(0);
                return true;
            }

        }
    }

    private int buscarCliente(int ticket) {
        int posicionCliente = -1;
        int i = 0;
        while (i < this.listaClientesNoAtendidos.size() && this.listaClientesNoAtendidos.get(i).getnTicket() != ticket) {
            i++;
        }

        if (i < this.listaClientesNoAtendidos.size()) {
            posicionCliente = i;
        }
        return posicionCliente;
    }

    public boolean clienteAbandona(int Ticket) {
        int posicionBuscar = this.buscarCliente(Ticket);
        if (posicionBuscar >= 0) {
            this.borrarCliente(this.listaClientesNoAtendidos.get(posicionBuscar));
            this.listaClientesNoAtendidos.remove(posicionBuscar);
            return true;
        }
        return false;
    }

    public boolean borrarCliente(Cliente cliente) {
        try {
            Connection miConexion = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "developer", "developer");
            Statement miStatement = miConexion.createStatement();
            String instruccionSql = "DELETE FROM CLIENTESNOATENDIDOS WHERE TICKET="+cliente.getnTicket();
            miStatement.executeUpdate(instruccionSql);
            return true;
        } catch (SQLException e) {
            System.out.println("Ha ocurrido un error al insertar el cliente en la base de datos");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public int adelantar(int ticket) {
        int posicion = this.buscarCliente(ticket);
        Cliente clientePos = null;
        Cliente clientePos2 = null;
        if (posicion > 0) {
            clientePos = this.listaClientesNoAtendidos.get(posicion);
            clientePos2 = this.listaClientesNoAtendidos.get(posicion - 1);
            this.listaClientesNoAtendidos.set(posicion - 1, clientePos);
            this.listaClientesNoAtendidos.set(posicion, clientePos2);
            return posicion;
        }
        return posicion;
    }

    public int retrasar(int ticket) {
        int posicion = this.buscarCliente(ticket);
        if (posicion >= 0 && posicion != this.listaClientesNoAtendidos.size() - 1) {
            Cliente clientepos = this.listaClientesNoAtendidos.get(posicion);
            this.listaClientesNoAtendidos.set(posicion, this.listaClientesNoAtendidos.get(posicion + 1));
            this.listaClientesNoAtendidos.set(posicion + 1, clientepos);
            return posicion;
        }

        return posicion;
    }

    public boolean dejarPasar() {
        if (this.listaClientesNoAtendidos.get(1) == null) {
            return false;
        } else {
            Cliente clienteDejaPasar = this.listaClientesNoAtendidos.get(0);
            this.listaClientesNoAtendidos.set(0, this.listaClientesNoAtendidos.get(1));
            this.listaClientesNoAtendidos.set(1, clienteDejaPasar);
            return true;
        }
    }

    @Override
    public String toString() {
        String frase = "";
        frase += "Fruteria " + this.nombre;
        frase += "\nClientes atendidos: " + this.contadorClientes;
        return frase;
    }
}
