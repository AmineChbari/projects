<script type="text/JavaScript">
        function Verif_taille()
        {
           var x = document.getElementById("taille_id").value;
           if (isNaN(x) == true) 
           {
              alert("Taille n'est pas valide / (1:4)");
              return false;
            } 
            else 
            {
              if (x>0 && x<=4) 
                {
                 return true;
                }
              else 
                {
                 alert("Taille n'est pas valide / (1:4)");
                 return false;
                }
             }
        }
</script> 
<?php 
if (isset($_GET['taille_se'])) {
  $fichier_R = fopen("reservations.txt","r");
  while(!feof($fichier_R))
  {
    $data= fgets($fichier_R);
    if (trim($data) == ""){ continue; }
    list($identifiant,$Nom,$taille_se,$sejour,$commentaire) = explode("|", $data);
      if ($taille_se == $_GET['taille_se']) {
        echo "<div class=\"w3-third w3-margin-bottom Ti$taille taille \" id=\"slidee\">
        <ul class=\"w3-ul w3-border w3-hover-shadow\">
          <li class=\"w3-theme\">
            <p class=\"w3-xlarge w3-allerta w3-\">Reservation $identifiant</p>
          </li>
          <li class=\"w3-padding-16\"><b>Nom:</b> $Nom</li>
          <li class=\"w3-padding-16\"><b>Taille de chambre:</b> $taille_se</li>
          <li class=\"w3-padding-16\"><b>Durée de Sejour:</b> $sejour</li>
          <li class=\"w3-padding-16\"><b>Durée de Sejour:</b> $commentaire</li>
          <li class=\"w3-theme-l5 w3-padding-24\">
            <div class=\"w3-row-padding\">
              <a class='w3-button w3-teal w3-padding-large w3-half' href='index.php?page=modifresa.php&identifiant=$identifiant'><i class=\"fa fa-pencil\"></i> Modifier</a>
              <a class='w3-button w3-red w3-padding-large w3-half' href='index.php?page=reservations.php&identifiant=$identifiant'><i class=\"fa fa-trash-o\"></i> Supprimer</a>          
            </div>
          </li>
        </ul>
      </div>";
      }
    }
    fclose($fichier_R);
      }
  ?>       
<?php
  if (isset($_GET['identifiant'])){
    $fichier_R = fopen("reservations.txt","r");
    $submit_data = "";
    while(!feof($fichier_R))
    {
      $data= fgets($fichier_R);
      if (trim($data) == ""){ continue; } 
      list($identifiant,$Nom,$taille,$sejour,$commentaire) = explode("|", $data);
      if ($identifiant != $_GET['identifiant']) {
        $submit_data .= $data;
      }
    }
    fclose($fichier_R);
    $fichier_R = fopen("reservations.txt","w+");
    fwrite($fichier_R, $submit_data);
    fclose($fichier_R);
    echo '<br><br><br><br><div class="w3-panel w3-card-4 w3-round-xlarge w3-teal w3-center w3-display-container">
      <span class="w3-button w3-large w3-display-topright w3-round-xlarge" onclick="this.parentElement.style.display=\'none\';">&times;</span> 
      <p>Reservation supprimée avec succès !</p>
    </div>';
  }





echo "<div class=\"w3-row-padding w3-center w3-padding-64 w3-display-container\">
    <h2 class='w3-allerta w3-wide w3-xxxlarge'>Vos Reservations</h2>";
      echo "<div class='w3-dropdown-hover w3-akhdori w3-left'>
        <button class='w3-button w3-animate-left'>Choisir Taille <i class='fa fa-angle-down'></i></button>
        <div class='w3-dropdown-content w3-bar-block w3-border '>
        <span class='w3-bar-item w3-btn w3-left w3-hover-grey' onclick='show(\"taille\")'>Tous</span><br>
        <span class='w3-bar-item w3-btn w3-left w3-hover-grey' onclick='show(\"Ti1\")'>Taille 1</span>
        <span class='w3-bar-item w3-btn w3-left w3-hover-grey' onclick='show(\"Ti2\")'>Taille 2</span>
        <span class='w3-bar-item w3-btn w3-left w3-hover-grey' onclick='show(\"Ti3\")'>Taille 3</span>
        <span class='w3-bar-item w3-btn w3-left w3-hover-grey' onclick='show(\"Ti4\")'>Taille 4</span>
      </div>
    </div>


    <div class='w3-dropdown-hover w3-akhdori w3-right'>
        <button class='w3-button w3-animate-right w3-right' onclick=\"document.getElementById('Modal_ch').style.display='block'\">Trouver votre reservation <i class='fa fa-search'></i></button>
    </div>
    <br><br><br>
    

    <script>
    function show(vrr) {
       var x = document.getElementsByClassName(\"taille\");
       var i;
       for (i = 0; i < x.length; i++) {
          x[i].style.display = 'none';
        }
       var x = document.getElementsByClassName(vrr);
       var i;
       for (i = 0; i < x.length; i++) {
          x[i].style.display = 'block';
        }
    }
    </script>";
    $duree = array();
    $duree[0] = 0;
    $cpt = array();
    $cpt[0] = 0;
    $fichier_R = fopen("reservations.txt", "r");
    
    while (!feof($fichier_R)) {
        $data = fgets($fichier_R);
        if (trim($data) == "") {
            continue;
        }
        list($identifiant, $Nom, $taille, $sejour, $commentaire) = explode("|", $data);
    
        if ($taille == 1) {
            $cpt[0] = $cpt[0] + 1;
            $duree[0] = $duree[0] + $sejour;
        }
    
        // Add similar blocks for other sizes (2, 3, 4) here
    
        echo "<div class=\"w3-third w3-margin-bottom Ti$taille taille\">
            <ul class=\"w3-ul w3-border w3-hover-shadow\">
              <li class=\"w3-theme\">
                <p class=\"w3-xlarge w3-allerta\">Reservation $identifiant</p>
              </li>
              <li class=\"w3-padding-16\"><b>Nom:</b> $Nom</li>
              <li class=\"w3-padding-16\"><b>Taille de chambre:</b> $taille</li>
              <li class=\"w3-padding-16\"><b>Durée de Séjour:</b> $sejour</li>
              <li class=\"w3-padding-16\"><b>Commentaire:</b> $commentaire</li>
              <li class=\"w3-theme-l5 w3-padding-24\">
                <div class=\"w3-row-padding\">
                  <a class='w3-button w3-teal w3-padding-large w3-half' href='index.php?page=modifresa.php&identifiant=$identifiant'><i class=\"fa fa-pencil\"></i> Modifier</a>
                  <a class='w3-button w3-red w3-padding-large w3-half' href='index.php?page=reservations.php&identifiant=$identifiant'><i class=\"fa fa-trash-o\"></i> Supprimer</a>
                </div>
              </li>
            </ul>
          </div>";
    }
    
    fclose($fichier_R);
    
    echo "<table class=\"table1\">
      <tr>
        <th class=\"th1\">Taille de la reservation</th>
        <th class=\"th1\">Moyenne</th>
      </tr>";
    for ($i = 0; $i < 4; $i++) {
        if (isset($duree[$i]) && isset($cpt[$i])) {
            echo "<tr>
        <td class=\"td1\">" . ($i + 1) . "</td>
        <td class=\"td1\">" . ($cpt[$i] > 0 ? $duree[$i] / $cpt[$i] : 0) . "</td>
        </tr>";
        }
    }
    echo "</table>";
?>




    <?php echo "<div class=\"w3-display-container w3-animate-opacity w3-modal w3-row\" id=\"Modal_ch\">
    <div class=\"w3-modal-content w3-half w3-padding-top\" style=\"width:50%;left:80px;\">
    <span class=\"w3-button w3-display-topright w3-round-large\" onclick=\"document.getElementById('Modal_ch').style.display='none';\">&times;</span>
    <header class=\"w3-allerta w3-akhdori w3-display-top\"><i>  Vos infos  </i></header>
    <form class=\"w3-container w3-margin\" method=\"POST\" action=\"index.php?page=reservations.php\" onsubmit=\"return Verif_taille()\">

        <label for=\"taille_se\" class=\"w3-allerta\">Taille de la Chambre (pour 1,2,3 ou 4 personnes)</label> <input type=\"text\" name=\"taille_se\" id=\"taille_id\"  class=\"w3-input taille_c\"<br><br>

                <button type=\"submit\" class=\"w3-btn w3-akhdori w3-allerta\">Chercher</button>
                <br>
    </form>
    </div>
  </div>";

if (isset($_POST['taille_se'])) {
  $reset = false;  
  $fichier_R = fopen("reservations.txt","r");
  while(!feof($fichier_R))
  {
    $data= fgets($fichier_R);
    if (trim($data) == ""){ continue; }
    list($identifiant,$Nom,$taille_se,$sejour,$commentaire) = explode("|", $data);
      if ($taille_se == $_POST['taille_se']) {
      echo "<script type=\"text/JavaScript\">show('Ti$taille_se');</script>";
      $reset = true; 
      }
    }
    if ($reset == true) {
      echo"<span class=\"w3-button w3-bottommiddle w3-grey w3-round-large \" onclick=\"show('taille')\">RESET</span>";
    }
    fclose($fichier_R);
      }
  ?>
  </div>


