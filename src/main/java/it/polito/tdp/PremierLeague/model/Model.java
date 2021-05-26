package it.polito.tdp.PremierLeague.model;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private Map<Integer, Player> idMap;
	
	public Model() {
		
		this.dao= new PremierLeagueDAO();
		this.idMap= new HashMap<Integer, Player>();
		this.dao.listAllPlayers(idMap);
		
	}
	
	public void creaGrafo(Match m) {
		
		this.grafo=new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//vertici
		
		//filtro sui vertici, query
		Graphs.addAllVertices(this.grafo, this.dao.getVertici(m, idMap));
		
		//archi
		for(Adiacenza a: dao.getAdiacenze(m, idMap)) {
			
			if(a.getPeso()>=0) {
				//p1 meglio di p2, arco da p1 a p2
				
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					Graphs.addEdgeWithVertices(this.grafo, a.getP1(), a.getP2(), a.getPeso());
				}
				
				
			}
			else {
				
				//p2 meglio di p1
				
				if(grafo.containsVertex(a.getP1()) && grafo.containsVertex(a.getP2())) {
					
					Graphs.addEdgeWithVertices(this.grafo, a.getP2(), a.getP1(), (-1)*a.getPeso());
				}
				
				
			}
			
			
			
		}
		
		
	}
	
	
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Match> getAllMatches(){
		
		List<Match> matches= dao.listAllMatches();
		Collections.sort(matches, new Comparator<Match>() {

			@Override
			public int compare(Match o1, Match o2) {
				
				return o1.getMatchID().compareTo(o2.getMatchID());
			}
			
		});
		
		return matches;
	}
	
	//punto e: sulla base del grafo creato, esraggo un giocatore con il suo delta di efficienza
	
	public GiocatoreMigliore getMigliore() {
		
		if(grafo==null) {
			return null;
		}
		
		//inizializzo 
		Player best=null;
		Double maxDelta= (double) Integer.MIN_VALUE; //anche 0 o -999
		
		for(Player p: this.grafo.vertexSet()) { 
			//scorro tutti i giocatori e calcolo: delta= somma pesi archi uscenti-somma pesi archi entranti
			
			//calcolo la somma dei pesi degli archi uscenti e entranti
			double pesoUscente=0.0;
			
			for(DefaultWeightedEdge edge: this.grafo.outgoingEdgesOf(p)) {
				pesoUscente+=this.grafo.getEdgeWeight(edge);
			}
			
			double pesoEntrante=0.0;
			
			for(DefaultWeightedEdge edge: this.grafo.incomingEdgesOf(p)){
				pesoEntrante+=this.grafo.getEdgeWeight(edge);
			}
			
			double delta=pesoUscente-pesoEntrante;
			
			if(delta> maxDelta) {
				best=p;
				maxDelta=delta;
			}
			
			
		}
		
		return new GiocatoreMigliore(best, maxDelta);
	}

	public Graph<Player, DefaultWeightedEdge> getGrafo() {
		
		return this.grafo;
	}
	
}
