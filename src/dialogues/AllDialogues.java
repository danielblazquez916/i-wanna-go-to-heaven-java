package dialogues;

public class AllDialogues {
    
    public static Node jokerDialogue(){

        String[] n1 = new String[]{
            "eeeeeeEEEEEEAAAAAAAAAAAAH",
            "¿Qué quieres? No ves que estoy ocupado pensando."
        }; 
        
        String p11 = "¿En qué piensas?";
        
        String[] n111 = new String[]{
            "En cosas muy importantes como averiguar la forma de salir de aquí.", 
            "En la superficie tengo que hablar con miembros de mi antiguo equipo."
        };
        
        String p1111 = "Wow, ¿qué clase de equipo?";
        
        String[] n11111 = new String[]{
            "Uno muy bueno. Empezó siendo algo pequeño, ya sabes, lo típico entre chavales...", 
            "Y al final formamos una gran comunidad con una ciudad en la superficie, se hacia llamar 'Comando Crack'.",
            "Pero ocurrieron problemas entre los lideres... Y aqui he acabado, expulsado por mi propia gente...",
            "Pero volveré con más fuerza... Quizá me veas al otro lado! 😉"
        };
        
        String p1112 = "Sinceramente, no me interesa.";
        
        String[] n11121 = new String[]{
            "A lo mejor si te interesa que te lance una omegabomba nuclearmente masiva más grande que el universo en si mismo que te penetre el ojete en 340 partes diferentes.",
            "Además, si tan poco te interesa, ¿pa que preguntas chaval? Vete a currar."
        };
        
        String p12 = "WOW, ERES EL JOKER!";
        
        String[] n121 = new String[]{
            "Sí... Bueno... más o menos.",
            "Pero me solían llamar 'jokerpower' (buenos tiempos atrás...)."
        };
        
        String p1211 = "¿jokerpower? ¿Y eso?";
        
        String[] n12111 = new String[]{
            "Políticas de Sony supongo, yo quería ponerme Joker pero ya sabes como va esto (o quizá no lo sabe...)",
            "Igualmente, se ha quedado como nombre clave entre mi equipo.",
            "Si al salir a la superficie nos vemos, te enseñaré la bandera. Saludos!!"
        };
        
        String p1212 = "Entonces no eres el verdadero...";
        
        String[] n12121 = new String[]{
            "¿COMO? Oye, yo soy el fundador de una ciudad alli afuera...",
            "Siempre seré EL VERDADERO aunque me expulsen...",
            "Ese desalmado... Alguien ha ocupado mi lugar... Tu seras testigo de la justicia."
        };
        
        String p13 = "Ups, adios.";
        
        String[] n131 = new String[]{
            "Adios supongo (se habrá confundido de tecla??...)"
        };
        
        // NODO RAIZ:
        Node npc1 = new Node(n1);
        
        // NODO 1: RAMIFICACION 1:
        Node player11 = new Node(p11);
        Node npc111 = new Node(n111);
        Node player1111 = new Node(p1111);
        Node npc11111 = new Node(n11111);
        Node player1112 = new Node(p1112);
        Node npc11121 = new Node(n11121);      

        // NODO 2: RAMIFICACION 2:
        Node player12 = new Node(p12);
        Node npc121 = new Node(n121);
        Node player1211 = new Node(p1211);
        Node npc12111 = new Node(n12111);
        Node player1212 = new Node(p1212);
        Node npc12121 = new Node(n12121);
        
        Node player13 = new Node(p13);
        Node npc131 = new Node(n131);
        
        npc1.addHijos(player11);
        
        player11.addHijos(npc111);
        npc111.addHijos(player1111);
        player1111.addHijos(npc11111);
        npc111.addHijos(player1112);
        player1112.addHijos(npc11121);
        
        npc1.addHijos(player12);
        
        player12.addHijos(npc121);
        npc121.addHijos(player1211);
        player1211.addHijos(npc12111);
        npc121.addHijos(player1212);
        player1212.addHijos(npc12121);
        
        npc1.addHijos(player13);
        player13.addHijos(npc131);
        
        return npc1;
    }
    
    public static Node nullDialogue(){
        String[] n1 = new String[]{
            "La luz que te envuelve y ampara ha estado vigilando tus caminos.", 
            "Esta luz consume todo aquello que pretende hacerte daño.",
            "No estás preparado para contemplar la plenitud de esta luz; mirar más allá de sus misterios es lo que te conducirá al desvarío.",
            "Saldrás de la cueva y enfrentarás a los guardianes de tu última esperanza.",
            "Cuatro signos te serán revelados: Poder, Justicia, Fuerza y Dominio. Este último te traerá desgracia.",
            "Que la gracia te acompañe."
        };
        
        Node npc1 = new Node(n1);
        
        return npc1;
    }
    
    
    public static Node blackManDialogue(){
        String[] n1 = new String[]{
            "Heey, ¿que pasa tio? ¿Todo bien?", 
            "Ándate con ojo por aqui, nunca sabes lo que te puedes encontrar jajaja."
        };
        
        String p11 = "Ya veo ya...";
        
        String[] n111 = new String[]{
            "Si, esos pinchos deben de doler... que se le va a hacer, ¿no?",
            "Bueno, ¿que plan tienes colega?"
        };
        
        String p1111 = "Mi plan es visitar a Dios.";
        
        String[] n11111 = new String[]{
            "...",
            "Pues me demuestras que eres imbecil, deberías empezar desde la superficie...",
            "Si empiezas por aqui vas a tardar más (piensa poco el chico...)."
        };
        
        String p1112 = "No tengo ningún plan.";
        
        String[] n11121 = new String[]{
            "Pues entonces vas mal encaminado, te lo digo yo.",
            "Lo más seguro es que mueras. Buena suerte."
        };
        
        //////////////////////////////////////////////////////////////////////////////////////////
        
        String p12 = "Tu baile me irrita.";
        
        String[] n121 = new String[]{
            "¿Cómo? Oye, pero si es genial!!!!",
            "Tienes un gusto horrible, vete a ver a auronplay."
        };
        
        String p1211 = "No me gusta auronplay.";
        
        String[] n12111 = new String[]{
            "Me da igual, tus gustos siguen siendo horribles.",
            "Ojala algún día cambies, sucio aventurero..."
        };
        
        String p1212 = "Prefiero ver a Toreman, gracias.";
        
        String[] n12121 = new String[]{
            "Ok, eso no cambia igualmente mi forma de verte.",
            "Ver coches volando por una pelota no te hace mejor, más bien te hace ver patético..."
        };
        
        String p1213 = "No veo a esos tipos... Soy sigma 🗿.";
        
        String[] n12131 = new String[]{
            "¿Sigma? tu miras skibidi toilet seguro, no pegas ni con cola aquí.",
            "Celebraré tu muerte cercana con mi baile, adios!"
        };
        
        /////////////////////////////////////////////////////////////////////////////////////////////////
        
        String p13 = "Tu también deberías andarte con ojo.";
        
        String[] n131 = new String[]{
            "Lo dudo, aquí soy el que manda (a veces al menos...).",
            "Así que yo no tengo porque tener miedo de nada."
        };
        
        String p1311 = "No mandas a nadie.";
        
        String[] n13111 = new String[]{
            "¿CÓMO? CLARO QUE MANDO!!",
            "YA ME HE HARTADO, NO ME TOMAS EN SERIO. VETE A TOMAR POR CULO."
        };
        
        String p1312 = "No me hagas nada 😨";
        
        String[] n13121 = new String[]{
            "Tranquilo, yo soy justo con la gente.",
            "Como pareces un buen tipo, te perdonaré la vida, pero otros no harían lo mismo..."
        };
        
        //////////////////////////////////////////////////////////////////////////////////////////////////
        
        String p14 = "Adios tio!";
        
        String[] n141 = new String[]{
            "Hasta nunca!"
        };
        
        // NODO RAIZ:
        Node npc1 = new Node(n1);
        
        // NODO 1: RAMIFICACION 1:
        Node player11 = new Node(p11);
        Node npc111 = new Node(n111);
        Node player1111 = new Node(p1111);
        Node npc11111 = new Node(n11111);
        Node player1112 = new Node(p1112);
        Node npc11121 = new Node(n11121);      

        // NODO 2: RAMIFICACION 2:
        Node player12 = new Node(p12);
        Node npc121 = new Node(n121);
        Node player1211 = new Node(p1211);
        Node npc12111 = new Node(n12111);
        Node player1212 = new Node(p1212);
        Node npc12121 = new Node(n12121);
        Node player1213 = new Node(p1213);
        Node npc12131 = new Node(n12131);
        
        // NODO 3: RAMIFICACION 3:
        Node player13 = new Node(p13);
        Node npc131 = new Node(n131);
        Node player1311 = new Node(p1311);
        Node npc13111 = new Node(n13111);
        Node player1312 = new Node(p1312);
        Node npc13121 = new Node(n13121);
        
        Node player14 = new Node(p14);
        Node npc141 = new Node(n141);
        
        npc1.addHijos(player11);
        
        player11.addHijos(npc111);
        npc111.addHijos(player1111);
        player1111.addHijos(npc11111);
        npc111.addHijos(player1112);
        player1112.addHijos(npc11121);
        
        npc1.addHijos(player12);
        
        player12.addHijos(npc121);
        npc121.addHijos(player1211);
        player1211.addHijos(npc12111);
        npc121.addHijos(player1212);
        player1212.addHijos(npc12121);
        npc121.addHijos(player1213);
        player1213.addHijos(npc12131);
        
        npc1.addHijos(player13);
        
        player13.addHijos(npc131);
        npc131.addHijos(player1311);
        player1311.addHijos(npc13111);
        npc131.addHijos(player1312);
        player1312.addHijos(npc13121);
        
        npc1.addHijos(player14);
        player14.addHijos(npc141);
        
        return npc1;
    }
    
    
    public static Node helloKittyDialogue(){
        // Creamos un arbol que representa 
        // todo el dialogo que tenemos:
        String[] dialogo1 = new String[]{
            "¿Quién se supone que eres?", 
            
            "No interrumpas mi sesión de running..."
        };
        
        String[] dialogo2 = new String[]{
            "Mmmm... pues no deberías merodear tanto...", 
            "Ocurren cosas malas por aqui..."
        };
        
        String[] dialogo3 = new String[]{
            "¿Aventurero? He visto a muchos de tu calaña.", 
            "Aunque ahora están todos muertos, una pena..."
        };
        
        String[] dialogo4 = new String[]{
            "¿QUÉ? ESO ES MENTIRA >:X", 
            "SEGURO QUE TÚ ERES UNA TORTUGA..."
        };
        
        String[] dialogo5 = new String[]{
            "¿Y para esto me interrumpes? Vete a tomar por culo."
        };
        
        Node raiz = new Node(dialogo1);
        
        Node raiz_opcion1 = new Node("Nadie... solo merodeaba por la zona...");
        Node opcion1_response = new Node(dialogo2);
        raiz_opcion1.addHijos(opcion1_response);
        
        Node raiz_opcion2 = new Node("Un aventurero.");
        Node opcion2_response = new Node(dialogo3);
        raiz_opcion2.addHijos(opcion2_response);
        
        Node raiz_opcion3 = new Node("Pero si vas super lento...");
        Node opcion3_response = new Node(dialogo4);
        raiz_opcion3.addHijos(opcion3_response);
        
        Node raiz_opcion4 = new Node("Hasta luego!");
        Node opcion4_response = new Node(dialogo5);
        raiz_opcion4.addHijos(opcion4_response);
        
        raiz.addHijos(raiz_opcion1);
        raiz.addHijos(raiz_opcion2);
        raiz.addHijos(raiz_opcion3);
        raiz.addHijos(raiz_opcion4);
        
        return raiz;
    }
    
    
}
