package org.insa.graphs.algorithm.shortestpath;

import java.util.*;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    protected Graph graph = data.getGraph();
    protected int nb_Nodes = graph.size();
    
    
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    protected Label[] labels_init() {

		Label[] labels = new Label[nb_Nodes];
        List<Node> nodes = graph.getNodes();
		for (Node node : nodes) {
			labels[node.getId()] = new Label(node);
		}
		return labels;
	}



    @Override
    protected ShortestPathSolution doRun() {
        final ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
        Label[] labels;

		// appel de la fonction: labels créés et initialisés
		labels = labels_init();

		// initialisation du tas
		// création du tas des labels
		BinaryHeap <Label> tas = new BinaryHeap<>();

		// Création des noeuds origine et destination avec leur labels
		Node noeud_origine = data.getOrigin();
		Label label_origine = labels[noeud_origine.getId()];
		Node Destination = data.getDestination();
		Label label_destination = labels[Destination.getId()];

		// insertion du noeud origine dans le tas
		label_origine.setCoutRealise(0); // On initialise son cout à 0;
		tas.insert(label_origine);

		notifyOriginProcessed(data.getOrigin());

		Label label_courant = null;


		
		while (!label_destination.getMarque() && !tas.isEmpty()) {

			
			label_courant = tas.deleteMin();

			// On marque le label extrait
			label_courant.setMarque(false);

			/* Notify observers about the node being marked */
			notifyNodeMarked(label_courant.getSommetCourant());

			// On crée une liste de successeurs du noeud courant pour simplifier leur Parcour
		
			List<Arc> successeurs = label_courant.getSommetCourant().getSuccessors();

			// Parcours de tous les successeurs du noeud courant
			for (Arc arc_courant : successeurs) {

				// Condition importante pour vérifier que l'arc en question est permis selon le mode de déplacement
				if (data.isAllowed(arc_courant)) {

					// déclaration du noeud destination de chaque arc
					Label destination_iter = labels[arc_courant.getDestination().getId()];

					/* Notify observers about the node being reached */
					notifyNodeReached(arc_courant.getDestination());

					// Si le noeud destination de l'arc n'est pas marqué
					if (!destination_iter.getMarque()) {

						// dans le cas où le coût du noeud destination est plus grand que le coup du
						// noeud courant + la longueur entre les deux noeuds:

						if (destination_iter.getCost() > label_courant.getCost() + data.getCost(arc_courant)) {

							// si le noeud destination de l'arc est déjà dans le tas, on l'enlève pour l'ajouter à la fin quand il sera màj)
							
							try {
								tas.remove(destination_iter);
							} catch (ElementNotFoundException e) {
							}

							// on met à jour le coût du noeud destination de l'arc
							destination_iter.setCoutRealise(label_courant.getCost() + data.getCost(arc_courant));
							// on met à jour son père qui devient le noeud courant on met tout l'arc car  c'est plus pratique)
						
							destination_iter.setPere(arc_courant);
							// on place le noeud destination de l'arc dans le tas
							tas.insert(destination_iter);
							


						}
					}
				}
			}
		}


		// On traite maintenant les cas restants où il n'y a pas de solution

		/*  Si le sommet destination n'a pas de prédecesseur ou si celui-ci n'a pas été
		marqué (l'un entraîne l'autre normalement),
		la solution n'existe pas*/
		if ((label_destination.getPere() == null && (data.getOrigin().compareTo(data.getDestination()) != 0))
				|| ! label_destination.getMarque()) {
			solution = new ShortestPathSolution(data, Status.INFEASIBLE);
		}
		// Sinon, on a trouvé la destination
		else {

			// User notification
			// The destination has been found, notify the observers.
			notifyDestinationReached(data.getDestination());

			// dans le cas oe le chemin est vide
			if (data.getOrigin().compareTo(data.getDestination()) == 0) {
				// solution
				// System.out.println("Le chemin est vide") ;
				solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, data.getOrigin()));

			} else {

				/*On reconstitue le chemin créé et on crée une liste contenant tous les arcs menants à la destination*/
				ArrayList<Arc> arcs = new ArrayList<>();
                /*On commence par la destination et en ajoute le père du noeud au fur et à mesure jusqu'à arriver au noeud origine 
				en mettant à jour le label courant*/
				while (!label_courant.equals(label_origine)) {
					arcs.add(label_courant.getPere());
					// maj du label courant
					label_courant = labels[label_courant.getPere().getOrigin().getId()];
				}
				/* On inverse le tout pour avoir le chemin de l'origine à la destination et pas
				l'inverse*/
				Collections.reverse(arcs);

				// Création de la solution finale à partir de la liste des arcs créée
				Path chemin_final = new Path(graph, arcs);

				// On teste si le chemin créé est valide avant de créer la solution finale
				if (chemin_final.isValid()) {

					// on crée la solution finale
					solution = new ShortestPathSolution(data, Status.OPTIMAL, chemin_final);

				} else {
					 System.out.println("Le chemin n'est pas valide") ;
				}

			}
		}
		return solution;
	}

    }

