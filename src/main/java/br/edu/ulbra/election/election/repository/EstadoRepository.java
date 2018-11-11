package br.edu.ulbra.election.election.repository;

import java.util.ArrayList;

public class EstadoRepository {

    private static ArrayList<String> loadEstados() {
        ArrayList<String> estados = new ArrayList<>();

        estados.add("AC");  /*Acre*/
        estados.add("AL"); /*ALAGOAS*/
        estados.add("AP"); /*AMAPA*/
        estados.add("AM"); /*AMAZONIA*/
        estados.add("BA"); /*BAHIA*/
        estados.add("CE"); /*CEARA*/
        estados.add("DF"); /*DISTRITO FEDERAL*/
        estados.add("ES");/*ESPITIRO SANTOS*/
        estados.add("GO"); /*GOIANIA*/
        estados.add("MA"); /*MARANHO*/
        estados.add("MT"); /*MATO GROSSO*/
        estados.add("MS"); /*MATO GROSSO DO SUL*/
        estados.add("MG"); /*MINAS GERAIS*/
        estados.add("PA"); /*PARA*/
        estados.add("PB"); /*PARAIBA*/
        estados.add("PR"); /*PARANA*/
        estados.add("PE"); /*PERNAMBUCO*/
        estados.add("PI"); /*PIAUI*/
        estados.add("RJ"); /*RIO DE JANEIRO*/
        estados.add("RN"); /*RIO GRANDE DO NORTE*/
        estados.add("RS"); /*RIO GRANDE DO SUL*/
        estados.add("RO"); /*RONDONIA*/
        estados.add("RR"); /*RORAIMA*/
        estados.add("SC"); /*SANTA CATARINA*/
        estados.add("SP"); /*SAO PAULO*/
        estados.add("SE"); /*SERGIPE*/
        estados.add("TO"); /*TOCANTIS*/

        estados.add("BR"); /*BRASIL*/

        return estados;
    }

    public static boolean estadoExits (String es) {
        ArrayList<String> estados = loadEstados();

        if(estados.contains(es)) {
            return true;
        }

        return false;
    }

}
