<header class="w3-container w3-theme ">
    


<nav class=" w3-theme-d1 w3-animate-top ">
    <div class="w3-bar"><i><span class="w3-left w3-bar-item w3-allerta">South Motel</span></i>
    
   
 <?php
        $onglets = array();
        $onglets["Accueil"] = "index.php?page=init.php";
        $onglets["Reserver"] = "index.php?page=modifresa.php";
        $onglets["Reservations"] = "index.php?page=reservations.php";

        echo "<div class=\"w3-right w3-hide-small\">";
        foreach ($onglets as $nom => $onglet) {
            if ($nom == "Accueil") {
                echo "  <a href=\"$onglet\" class='w3-bar-item w3-button w3-allerta'><i class='fa fa-home'></i> $nom</a>";
            }
            else {       
             echo "  <a href=\"$onglet\" class='w3-bar-item w3-button w3-allerta'>$nom</a>";
            }
        }           
        echo "</div>";
      ?>
      </div>
      
      



</nav>
</header>