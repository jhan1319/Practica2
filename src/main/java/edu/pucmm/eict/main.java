package edu.pucmm.eict;

import io.javalin.Javalin;
import io.javalin.plugin.rendering.JavalinRenderer;
import io.javalin.plugin.rendering.template.JavalinThymeleaf;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.ObjectName;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class main {

    public static void main(String[] args){

        Tienda controllerProducts = new Tienda();

        CarroCompra controllerCarro = new CarroCompra();

        /////AGREGANDO PRODUCTOS/////////

        Producto productoA = new Producto( "cocacola", 30, 10);
        Producto productoB = new Producto( "papita", 35, 20);
        Producto productoC= new Producto( "chicle", 15, 30);

        controllerProducts.agregarProductoStock("cocacola", 30, 10);
        controllerProducts.agregarProductoStock("papita", 35, 20);
        controllerProducts.agregarProductoStock("chicle", 15, 30);
        ///////CREANDO UN CARRITO DE COMPRAS//////////



        ////////INGRESANDO PRODUCTOS DE ARRIBA AL CARRITO CREADO/////



        ////////MOSTRANDO PRODUCTOS DEL CARRITO DE ARRIBA///////////

        //System.out.println("Los productos del carrito son: " + carroA.listaProductos.get(0).nombre);


        //Inicializando javalin



        Javalin app = Javalin.create( config -> {
            //set configs
            config.addStaticFiles("/Public");

        }).start(7000);

        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
        ///Primera vista de la página

        app.get("/", ctx -> {
            int cant = 0;
            Map<String, Object> modelo = new HashMap<>();
            modelo.put("Productos", controllerProducts.getProductosController());

            ctx.sessionAttribute("STOCK", controllerProducts);//ESTA ES LA SESIÓN DEL STOCK DE PRODUCTOS EN TODA LA TIENDA

            if (ctx.sessionAttribute("carritoCreado") != null){

                CarroCompra carrito = ctx.sessionAttribute("carritoCreado");

                for (Producto prod:
                        carrito.getListaProductos()) {

                    cant += prod.quantity;
                }
            } else {
                cant = 0;
            }
            ctx.sessionAttribute("CANTI", cant);
            System.out.println("LA CANTIDAD ES "+ cant);
            modelo.put("CANTIDAD",cant);
            ctx.render("/Templates/ListadoProductos.html", modelo);



        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////////





        app.get("/Autenticar/*", ctx -> {

            if (ctx.sessionAttribute("autenticado") == null){

                ctx.render("/Templates/Login.html");

            } else{







                String url = ctx.req.getRequestURI();

                if(url.equalsIgnoreCase("/Autenticar/ventas")){
                    /*
                    CarroCompra carroUser = ctx.sessionAttribute("carritoCreado");
                    Date date = new Date();
                    VentasProductos ventas = new VentasProductos(date, ctx.sessionAttribute("COMPRADOR"),  carroUser);

                    Tienda controller = ctx.sessionAttribute("STOCK");

                    controller.agregarVentas(ventas);*/

                    HashMap<String, Object> model = new HashMap<>();
                    model.put("ventas", controllerProducts.ventasProductosController);
                    model.put("cantidad", ctx.sessionAttribute("CANTI"));
                    ctx.render("/Templates/Ventas.html", model);



                };
                if(url.equalsIgnoreCase("/Autenticar/productos")){
                    Tienda stock2 = ctx.sessionAttribute("STOCK");
                    ArrayList<Producto> stock = stock2.getProductosController();
                    HashMap<String, Object> model = new HashMap<>();

                    model.put("stock", stock);

                    model.put("cantidad", ctx.sessionAttribute("CANTI"));


                    ctx.render("/Templates/CrudProductos.html", model);

                };





                //System.out.printf("\nYA HABIAS INICIADO SESION PAPUS\n Este es ese código dique URL: " + ctx.req.getRequestURI());
                //AQUI VA LA MANIOBRA PARA VOLVER A LA PAGINA ANTERIOR (WORKING ON IT)







            };
        });

        app.post("/ProcesarCompra", ctx -> {

            String nombre = ctx.formParam("nombreUser");

            ctx.sessionAttribute("COMPRADOR",nombre);

            ctx.redirect("/");

            CarroCompra carroUser = ctx.sessionAttribute("carritoCreado");
            Date date = new Date();
            VentasProductos ventas = new VentasProductos(date, ctx.sessionAttribute("COMPRADOR"),  carroUser);

            controllerProducts.agregarVentas(ventas);

            ctx.sessionAttribute("carritoCreado", null);


        });

        app.post("/CrearProducto", ctx -> {

            Producto prod = new Producto("", 0, 99);
            prod.setId(0);
            HashMap<String, Object> map = new HashMap<>();
            map.put("modificable",prod);
            ctx.render("/Templates/CrearProducto.html", map);
            //ctx.redirect("");




        });

        app.get("/Modificar/mod/*", ctx -> {

            String url = ctx.req.getRequestURI();


            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(url);
            int id = 0;
            while(m.find()) {
                id = Integer.parseInt(m.group());
            };

            System.out.println("ESTE ES EL ID "+id);
               if (id != 0){

                   Tienda tienda = ctx.sessionAttribute("STOCK");



                   Producto prod = tienda.prodByID(id);

                   ArrayList<Producto> productoVisualizar = new ArrayList<>();

                   //productoVisualizar.add(prod);

                   HashMap<String, Object> map = new HashMap<>();
                   map.put("modificable",prod);
                   ctx.render("/Templates/CrearProducto.html", map);


               }


        });

        app.get("/Modificar/borrar/*", ctx -> {
            String url = ctx.req.getRequestURI();


            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(url);
            int id = 0;
            while(m.find()) {
                id = Integer.parseInt(m.group());
            };

            if (id != 0){
                Tienda tienda = ctx.sessionAttribute("STOCK");

                System.out.println("ESTE ES EL ID "+id);

                ArrayList<Producto> productoss = tienda.getProductosController();

                productoss.remove(tienda.prodByID(id));

                HashMap<String, Object> map = new HashMap<>();
                map.put("stock",productoss);
                ctx.render("/Templates/CrudProductos.html", map);



            }




        });


        app.post("/Crear" , ctx -> {
            Tienda stock = ctx.sessionAttribute("STOCK");
            String nombre = ctx.formParam("nombreNuevo");
            Float precio = Float.parseFloat(ctx.formParam("precioNuevo"));
            int cant = 99;
            int id = Integer.parseInt(ctx.formParam("idNuevo"));
            if(id != 0){ //PARA MODIFICAR

                Producto prod = stock.prodByID(id);
                prod.setNombre(nombre);
                prod.setPrecio(precio);
                prod.setQuantity(99);

            }else {
                stock.agregarProductoStock(nombre,precio,cant);
            }

            HashMap<String, Object> model = new HashMap<>();

            model.put("stock", stock.getProductosController());

            ctx.render("/Templates/CrudProductos.html", model);




        });




        app.post("/Iniciarsesion", ctx -> {

            String nombre = ctx.formParam("USERNAME");
            String password = ctx.formParam("PASSWORD");
            System.out.println("ESTOS SON LOS PARAMETROS INGRESADOS: "+nombre+"   "+password);

            if (nombre.contentEquals("admin") & password.contentEquals("admin") ){

                System.out.printf("\nFELICIDADES, INICIASTE SESIÓN BRODEL\n");
                Usuario user = new Usuario("admin", "jhan", "admin");

                ctx.sessionAttribute("autenticado",user);
                ctx.redirect("/");



            };


        });













        /////////////////////////////////////////////////////////////////////////////////////////////////////////


        app.get("/carrito", ctx -> {

            if(ctx.sessionAttribute("carritoCreado") == null){
                CarroCompra carroUser = new CarroCompra();
                ctx.sessionAttribute("carritoCreado", carroUser);
                System.out.printf(" CREADA A SESIÓN DEL CARRITO");
                ctx.render("Templates/Carrito_Compra.html");
            } else {
                CarroCompra miCarrito = ctx.sessionAttribute("carritoCreado");

                HashMap<String, Object> mapaCarrito = new HashMap<>();
                mapaCarrito.put("miCarrito", miCarrito.getListaProductos());
                mapaCarrito.put("cantidad", ctx.sessionAttribute("CANTI"));
                ctx.render("Templates/Carrito_Compra.html", mapaCarrito);
            }






        });

        app.post("/eliminar", ctx -> {
           CarroCompra miCarrito = ctx.sessionAttribute("carritoCreado");
            int idd = Integer.parseInt(ctx.formParam("productoID"));

            for (Producto prod:
                 miCarrito.getListaProductos()) {



                if((prod.id == Integer.parseInt(ctx.formParam("productoID")) && ctx.formParam("nombreProducto").equalsIgnoreCase(prod.nombre)))
                {
                    System.out.println("PRODUCTO BORRADO");
                    System.out.println(prod.id);
                    miCarrito.eliminarProductoCarrito(prod.id);
                    break;
                }
            }

            HashMap<String, Object> modelo = new HashMap<>();
            modelo.put("miCarrito", miCarrito.getListaProductos());



            ctx.render("/Templates/Carrito_Compra.html", modelo);

        });

        app.post("/Agregar", ctx -> {

            String nombreProd = ctx.formParam("nombreProd");

            float precioProd = Float.parseFloat(ctx.formParam("precioProd").toString());

            int cantProd = Integer.parseInt(ctx.formParam("cantProd").toString());

            //PRODUCTO DE WEN CREADO



            //Crear la sesión pal carrito y verificación

            if(ctx.sessionAttribute("carritoCreado") == null){
                CarroCompra carroUser = new CarroCompra();
                carroUser.agregarProductoCarrito(nombreProd,precioProd,cantProd);
                ctx.sessionAttribute("carritoCreado", carroUser); //CREADA LA SESIÓN
                System.out.printf(" CREADA A SESIÓN DEL CARRITO");
            } else {

                CarroCompra carritoExistente = ctx.sessionAttribute("carritoCreado");
                carritoExistente.agregarProductoCarrito(nombreProd,precioProd,cantProd);

            }
            ctx.redirect("/");

        });

        app.get("/LimpiarCarro", ctx -> {

            CarroCompra carroUser = new CarroCompra();
            carroUser = ctx.sessionAttribute("carritoCreado");
            carroUser.limpiarCarrito();

            System.out.println("CARRITO VACIADO CON EXITO MIOP JEJE ADIOS COMPRA");

            ctx.render("/Templates/Carrito_Compra.html");



        });



    }












}
