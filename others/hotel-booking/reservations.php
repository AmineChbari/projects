<script type="text/JavaScript">
        function Verif_taille()
        {
           var x = document.getElementById("taille_id").value;
           if (isNaN(x) || x < 1 || x > 4)
           {
              alert("Taille n'est pas valide / (1:4)");
              return false;
           }
           return true;
        }
</script>
<?php
if (isset($_GET['taille_se'])) {
  $taille_filter = intval($_GET['taille_se']);
  if ($taille_filter >= 1 && $taille_filter <= 4) {
    $fichier_R = fopen("reservations.txt","r");
    while(!feof($fichier_R))
    {
      $data = fgets($fichier_R);
      if (trim($data) == ""){ continue; }
      $parts = explode("|", $data);
      if (count($parts) < 5) { continue; }
      list($identifiant, $Nom, $taille_se, $sejour, $commentaire) = $parts;
      if (intval($taille_se) === $taille_filter) {
        $id_s  = htmlspecialchars($identifiant, ENT_QUOTES, 'UTF-8');
        $nom_s = htmlspecialchars($Nom,         ENT_QUOTES, 'UTF-8');
        $ts_s  = htmlspecialchars($taille_se,   ENT_QUOTES, 'UTF-8');
        $sej_s = htmlspecialchars($sejour,      ENT_QUOTES, 'UTF-8');
        $com_s = htmlspecialchars($commentaire, ENT_QUOTES, 'UTF-8');
        echo "<div class=\"w3-third w3-margin-bottom Ti{$ts_s} taille\" id=\"slidee\">
        <ul class=\"w3-ul w3-border w3-hover-shadow\">
          <li class=\"w3-theme\">
            <p class=\"w3-xlarge w3-allerta\">Reservation {$id_s}</p>
          </li>
          <li class=\"w3-padding-16\"><b>Nom:</b> {$nom_s}</li>
          <li class=\"w3-padding-16\"><b>Taille de chambre:</b> {$ts_s}</li>
          <li class=\"w3-padding-16\"><b>Durée de séjour:</b> {$sej_s} jour(s)</li>
          <li class=\"w3-padding-16\"><b>Commentaire:</b> {$com_s}</li>
          <li class=\"w3-theme-l5 w3-padding-24\">
            <div class=\"w3-row-padding\">
              <a class='w3-button w3-teal w3-padding-large w3-half' href='index.php?page=modifresa.php&identifiant={$id_s}'><i class=\"fa fa-pencil\"></i> Modifier</a>
              <a class='w3-button w3-red w3-padding-large w3-half' href='index.php?page=reservations.php&identifiant={$id_s}' onclick=\"return confirm('Supprimer cette réservation ?')\"><i class=\"fa fa-trash-o\"></i> Supprimer</a>
            </div>
          </li>
        </ul>
      </div>";
      }
    }
    fclose($fichier_R);
  }
}
?>
<?php
  if (isset($_GET['identifiant'])){
    $del_id = intval($_GET['identifiant']);
    $fichier_R = fopen("reservations.txt","r");
    $submit_data = "";
    while(!feof($fichier_R))
    {
      $data = fgets($fichier_R);
      if (trim($data) == ""){ continue; }
      $parts = explode("|", $data);
      if (count($parts) < 5) { continue; }
      list($fid, $fNom, $ftaille, $fsejour, $fcommentaire) = $parts;
      if (intval($fid) !== $del_id) {
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
        <div class='w3-dropdown-content w3-bar-block w3-border'>
        <span class='w3-bar-item w3-btn w3-left w3-hover-grey' onclick='show(\"taille\")'>Tous</span><br>
        <span class='w3-bar-item w3-btn w3-left w3-hover-grey' onclick='show(\"Ti1\")'>Taille 1</span>
        <span class='w3-bar-item w3-btn w3-left w3-hover-grey' onclick='show(\"Ti2\")'>Taille 2</span>
        <span class='w3-bar-item w3-btn w3-left w3-hover-grey' onclick='show(\"Ti3\")'>Taille 3</span>
        <span class='w3-bar-item w3-btn w3-left w3-hover-grey' onclick='show(\"Ti4\")'>Taille 4</span>
      </div>
    </div>

    <div class='w3-dropdown-hover w3-akhdori w3-right'>
        <button class='w3-button w3-animate-right w3-right' onclick=\"document.getElementById('Modal_ch').style.display='block'\">Trouver votre réservation <i class='fa fa-search'></i></button>
    </div>
    <br><br><br>

    <script>
    function show(vrr) {
       var elems = document.getElementsByClassName('taille');
       for (var i = 0; i < elems.length; i++) { elems[i].style.display = 'none'; }
       var elems2 = document.getElementsByClassName(vrr);
       for (var i = 0; i < elems2.length; i++) { elems2[i].style.display = 'block'; }
    }
    </script>";

    // Stats: track all 4 room sizes
    $duree = [0, 0, 0, 0];
    $cpt   = [0, 0, 0, 0];

    $fichier_R = fopen("reservations.txt", "r");
    while (!feof($fichier_R)) {
        $data = fgets($fichier_R);
        if (trim($data) == "") { continue; }
        $parts = explode("|", $data);
        if (count($parts) < 5) { continue; }
        list($identifiant, $Nom, $taille, $sejour, $commentaire) = $parts;

        $t = intval($taille);
        if ($t >= 1 && $t <= 4) {
            $cpt[$t - 1]++;
            $duree[$t - 1] += intval($sejour);
        }

        $id_s  = htmlspecialchars(trim($identifiant), ENT_QUOTES, 'UTF-8');
        $nom_s = htmlspecialchars(trim($Nom),         ENT_QUOTES, 'UTF-8');
        $t_s   = htmlspecialchars(trim($taille),      ENT_QUOTES, 'UTF-8');
        $sej_s = htmlspecialchars(trim($sejour),      ENT_QUOTES, 'UTF-8');
        $com_s = htmlspecialchars(trim($commentaire), ENT_QUOTES, 'UTF-8');

        echo "<div class=\"w3-third w3-margin-bottom Ti{$t_s} taille\">
            <ul class=\"w3-ul w3-border w3-hover-shadow\">
              <li class=\"w3-theme\">
                <p class=\"w3-xlarge w3-allerta\">Reservation {$id_s}</p>
              </li>
              <li class=\"w3-padding-16\"><b>Nom:</b> {$nom_s}</li>
              <li class=\"w3-padding-16\"><b>Taille de chambre:</b> {$t_s} personne(s)</li>
              <li class=\"w3-padding-16\"><b>Durée de séjour:</b> {$sej_s} jour(s)</li>
              <li class=\"w3-padding-16\"><b>Commentaire:</b> {$com_s}</li>
              <li class=\"w3-theme-l5 w3-padding-24\">
                <div class=\"w3-row-padding\">
                  <a class='w3-button w3-teal w3-padding-large w3-half' href='index.php?page=modifresa.php&identifiant={$id_s}'><i class=\"fa fa-pencil\"></i> Modifier</a>
                  <a class='w3-button w3-red w3-padding-large w3-half' href='index.php?page=reservations.php&identifiant={$id_s}' onclick=\"return confirm('Supprimer cette réservation ?')\"><i class=\"fa fa-trash-o\"></i> Supprimer</a>
                </div>
              </li>
            </ul>
          </div>";
    }
    fclose($fichier_R);

    echo "<table class=\"table1\">
      <tr>
        <th class=\"th1\">Taille chambre</th>
        <th class=\"th1\">Nb réservations</th>
        <th class=\"th1\">Durée moy. (jours)</th>
      </tr>";
    for ($i = 0; $i < 4; $i++) {
        $moyenne = $cpt[$i] > 0 ? round($duree[$i] / $cpt[$i], 1) : 0;
        echo "<tr>
        <td class=\"td1\">" . ($i + 1) . " personne(s)</td>
        <td class=\"td1\">" . $cpt[$i] . "</td>
        <td class=\"td1\">" . $moyenne . "</td>
        </tr>";
    }
    echo "</table>";
?>

    <?php echo "<div class=\"w3-display-container w3-animate-opacity w3-modal w3-row\" id=\"Modal_ch\">
    <div class=\"w3-modal-content w3-half w3-padding-top\" style=\"width:50%;left:80px;\">
    <span class=\"w3-button w3-display-topright w3-round-large\" onclick=\"document.getElementById('Modal_ch').style.display='none';\">&times;</span>
    <header class=\"w3-allerta w3-akhdori w3-display-top\"><i>  Vos infos  </i></header>
    <form class=\"w3-container w3-margin\" method=\"POST\" action=\"index.php?page=reservations.php\" onsubmit=\"return Verif_taille()\">
        <label for=\"taille_se\" class=\"w3-allerta\">Taille de la Chambre (1, 2, 3 ou 4 personnes)</label>
        <input type=\"text\" name=\"taille_se\" id=\"taille_id\" class=\"w3-input taille_c\"><br><br>
        <button type=\"submit\" class=\"w3-btn w3-akhdori w3-allerta\">Chercher</button>
        <br>
    </form>
    </div>
  </div>";

if (isset($_POST['taille_se'])) {
  $taille_post = intval($_POST['taille_se']);
  if ($taille_post >= 1 && $taille_post <= 4) {
    $found = false;
    $fichier_R = fopen("reservations.txt","r");
    while(!feof($fichier_R))
    {
      $data = fgets($fichier_R);
      if (trim($data) == ""){ continue; }
      $parts = explode("|", $data);
      if (count($parts) < 5) { continue; }
      list($fid, $fNom, $ftaille_se, $fsejour, $fcommentaire) = $parts;
      if (intval($ftaille_se) === $taille_post) {
        echo "<script type=\"text/JavaScript\">show('Ti{$taille_post}');</script>";
        $found = true;
        break;
      }
    }
    fclose($fichier_R);
    if ($found) {
      echo "<span class=\"w3-button w3-bottommiddle w3-grey w3-round-large\" onclick=\"show('taille')\">RESET</span>";
    }
  }
}
?>
  </div>
