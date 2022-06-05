package Fruteria;

import org.junit.Assert;
import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FruteriaTest {

    public FruteriaTest() {
    }
    
    //comprobamos el metodo insertar cliente en tabla atendidos
    @Test
    public void testInsertarEnTablaAtendidos(){
        //Mockeamos un objeto cliente para comprobar si la insercion en bbdd se esta haciendo correctamente
    Fruteria f1 = new Fruteria("Sulca");
    int valorTicket=12;
    Cliente clienteMock= mock(Cliente.class);
    when(clienteMock.getnTicket()).thenReturn(valorTicket);
    when(clienteMock.getEdad()).thenReturn(Edad.MAYOR);
    assertTrue(f1.insertarEnTablaAtendidos(clienteMock));
    }
    
    //comprobamos el metodo insertar cliente en tabla no atendidos
    
    @Test
    public void testInsertarEnTablaNoAtendidos(){
        //Mockeamos un objeto cliente para comprobar si la insercion en bbdd se esta haciendo correctamente
    Fruteria f1 = new Fruteria("Sulca");
    int valorTicket=5;
    Cliente clienteMock= mock(Cliente.class);
    when(clienteMock.getnTicket()).thenReturn(valorTicket);
    when(clienteMock.getEdad()).thenReturn(Edad.JOVEN);
    assertTrue(f1.insertarEnTablaNoAtendidos(clienteMock));
    }
    
    //comprobamos el metodo borrarCliente
    
    
    @Test
    public void testBorrarClienteNoAtendidos(){
        //Mockeamos un objeto cliente para comprobar si la insercion en bbdd se esta haciendo correctamente
    Fruteria f1 = new Fruteria("Sulca");
    f1.insertarEnTablaNoAtendidos(new Cliente(6, Edad.JOVEN));
    int valorTicket=6;
    Cliente clienteMock= mock(Cliente.class);
    when(clienteMock.getnTicket()).thenReturn(valorTicket);
    when(clienteMock.getEdad()).thenReturn(Edad.JOVEN);
    assertTrue(f1.borrarCliente(clienteMock));
    }
    
    
    
    @Test
    public void testNuevoCliente() {
        //Comprobar que los tickets se asignan de manera correlativa

        Fruteria f1 = new Fruteria("Sulca");
        f1.nuevoCliente(Edad.MAYOR);
        f1.nuevoCliente(Edad.JOVEN);

        assertNotNull(f1.getListaClientesNoAtendidos());
    }

    /**
     * Test of atenderCliente method, of class Fruteria.
     */
    @Test
    public void testAtenderCliente() {

        Fruteria f1 = new Fruteria("Sulca");
        f1.nuevoCliente(Edad.MAYOR);
        f1.atenderCliente();
        assertFalse(!f1.getListaClientesNoAtendidos().isEmpty());

    }

    /**
     * Test of clienteAbandona method, of class Fruteria.
     */
    @Test
    public void testClienteAbandona() {

        Fruteria fx = new Fruteria("Sulca");
        fx.nuevoCliente(Edad.MAYOR);
        fx.nuevoCliente(Edad.JOVEN);
        fx.nuevoCliente(Edad.JOVEN);

        fx.clienteAbandona(1);
        fx.clienteAbandona(2);

        int ticket = fx.getListaClientesNoAtendidos().get(0).getnTicket();
        int ticket2 = 3;

        Assert.assertEquals(ticket, ticket2);//Comprueba que el ticket corresponda al tercer cliente 

    }

    @Test
    public void testClienteAbandonayDesapareceDeLaLista() {
        Fruteria f1 = new Fruteria("Sulca");
        f1.nuevoCliente(Edad.MAYOR);
        ArrayList<Cliente> copia = (ArrayList<Cliente>) f1.getListaClientesNoAtendidos().clone();
        f1.clienteAbandona(f1.getListaClientesNoAtendidos().get(0).getnTicket());
        assertNotEquals(copia, f1.getListaClientesNoAtendidos());

    }

    /**
     * Test of adelantar method, of class Fruteria.
     */
    @Test
    public void testAdelantar() {
        Fruteria prueba = new Fruteria("La lolita");
        prueba.nuevoCliente(Edad.JOVEN);
        prueba.nuevoCliente(Edad.JOVEN);
        prueba.adelantar(2);
        Assert.assertEquals(2, prueba.getListaClientesNoAtendidos().get(0).getnTicket());
   
        //Creamos dos clientes, el que va delante deja pasar al de atras
    }

    /**
     * Test of retrasar method, of class Fruteria.
     */
    @Test
    public void testRetrasar() {

        Fruteria fx = new Fruteria("Sulca");
        //Creamos dos clientes, uno deja pasar al que tiene detras
        fx.nuevoCliente(Edad.MAYOR);
        fx.nuevoCliente(Edad.JOVEN);

        int ticket1 = fx.getListaClientesNoAtendidos().get(0).getnTicket();
        int ticket2 = fx.getListaClientesNoAtendidos().get(1).getnTicket();

        fx.retrasar(ticket1);
        Assert.assertEquals(ticket1, fx.getListaClientesNoAtendidos().get(1).getnTicket());//Comprueba que el cliente retrasado concuerde con el numero del ticket de la posicion siguiente

    }

    /**
     * Test of dejarPasar method, of class Fruteria.
     */
    @Test
    public void testDejarPasarComprobarPosicionYEdad() {
        Fruteria fx = new Fruteria("Juan");
        fx.nuevoCliente(Edad.JOVEN);
        fx.nuevoCliente(Edad.MAYOR);
        
        fx.dejarPasar();
        //comprobamos que el ticket de la posicion 1 efectivamenta ya no es la misma de antes 
        assertNotEquals(fx.getListaClientesNoAtendidos().get(1),fx.getListaClientesNoAtendidos().get(0).getnTicket() );
        //nos aseguramos de que el vcalor de la edad es Mayor
        assertTrue(fx.getListaClientesNoAtendidos().get(0).getEdad()==Edad.MAYOR);

}

}
