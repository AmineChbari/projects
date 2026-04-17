
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

        function Verif_sejour()
        {
           var x = document.getElementById("sejour_id").value;
           if (isNaN(x) || x < 1 || x > 10)
           {
               alert("Durée de séjour n'est pas valide / (1:10)");
               return false;
            }
            return true;
        }

        function Verif_commentaire()
        {
           var x = document.getElementById("commentaire_id").value;
           if (x.length > 120)
           {
               alert("Commentaire trop long (120 caractères max)");
               return false;
            }
            return true;
        }

        function Subm() {
            return Verif_taille() && Verif_sejour() && Verif_commentaire();
        }

        function ReKey(event) {
            var unicode = event.which;
            if (unicode == 13) { return Subm(); }
        }
</script>

<?php

  // Validate identifiant from GET
  $identifiant = isset($_GET['identifiant']) ? intval($_GET['identifiant']) : 0;

  $fichier_R = fopen("reservations.txt","r");
  $valide_id = false;
  $submit_data = "";
  while(!feof($fichier_R))
  {
    $data = fgets($fichier_R);

    if (trim($data) == ""){
      continue;
    }
    $parts = explode("|", $data);
    if (count($parts) < 5) { continue; }
    list($fid, $fNom, $ftaille, $fsejour, $fcommentaire) = $parts;

    if ($identifiant > 0 && isset($_GET['identifiant'])) {
      if (intval($fid) !== $identifiant) {
        $submit_data .= $data;
      } else {
        if (isset($_POST['taille'])){
          // Server-side validation
          $taille_val      = intval($_POST['taille']);
          $sejour_val      = intval($_POST['sejour']);
          $nom_val         = trim($_POST['Nom']);
          $commentaire_val = trim($_POST['commentaire']);

          if ($taille_val < 1 || $taille_val > 4 ||
              $sejour_val < 1 || $sejour_val > 10 ||
              empty($nom_val) || strlen($commentaire_val) > 120) {
            fclose($fichier_R);
            die('<div class="w3-panel w3-red"><p>Données invalides.</p></div>');
          }

          // Sanitize: strip pipe characters to protect delimiter
          $nom_safe         = str_replace('|', '', $nom_val);
          $commentaire_safe = str_replace('|', '', $commentaire_val);

          $change_data = $identifiant.'|'.$nom_safe.'|'.$taille_val.'|'.$sejour_val.'|'.$commentaire_safe."\n";
          $submit_data .= $change_data;
          $valide_id = true;
        } else {
          $fNom         = trim($fNom);
          $ftaille      = trim($ftaille);
          $fsejour      = trim($fsejour);
          $fcommentaire = trim($fcommentaire);
          break;
        }
      }
    }
  }
  fclose($fichier_R);

  if (isset($_POST['taille'])){
    if (!$valide_id){
      $taille_val      = intval($_POST['taille']);
      $sejour_val      = intval($_POST['sejour']);
      $nom_safe        = str_replace('|', '', trim($_POST['Nom']));
      $commentaire_safe = str_replace('|', '', trim($_POST['commentaire']));

      if ($taille_val < 1 || $taille_val > 4 ||
          $sejour_val < 1 || $sejour_val > 10 ||
          empty($nom_safe) || strlen($commentaire_safe) > 120) {
        die('<div class="w3-panel w3-red"><p>Données invalides.</p></div>');
      }

      $change_data = "\n".$identifiant.'|'.$nom_safe.'|'.$taille_val.'|'.$sejour_val.'|'.$commentaire_safe."\n";
      $submit_data .= $change_data;
    }

    $fichier_R = fopen("reservations.txt","w+");
    fwrite($fichier_R, $submit_data);
    fclose($fichier_R);
    echo '<br><br><br><br><div class="w3-panel w3-card-4 w3-round-xlarge w3-green w3-center w3-display-container">
      <a class="w3-button w3-large w3-display-topright w3-round-xlarge" href=\'index.php?page=reservations.php\'>&times;</a>
      <p>Reservation est soumise avec succès !</p>
    </div>';
    }
    else {
    if (!isset($_GET['identifiant'])){
      $identifiant++;
      $fNom="";
      $ftaille="";
      $fsejour="";
      $fcommentaire="";
     }
?>



  <img src="IMG/r.jpg" alt="Lights" style="width:100%" class="w3-display-container">
  <br>
  <div class="w3-card-4 w3-display-container w3-margin w3-white  w3-animate-opacity w3-display-left" style="left:80px;">
    <header class="w3-allerta w3-black w3-display-top"><i>  Reservation   # <?php echo htmlspecialchars($identifiant, ENT_QUOTES, 'UTF-8'); ?> </i></header>
    <form class="w3-container w3-padding-32"  onsubmit="return Subm()" method="POST" action="index.php?page=modifresa.php&identifiant=<?php echo htmlspecialchars($identifiant, ENT_QUOTES, 'UTF-8'); ?>">

        <label for="Nom" class="w3-fate7 w3-allerta">NOM</label>
        <input type="text" name="Nom" id="Nom" class="w3-input" onkeypress="ReKey(event)"
          value="<?php echo htmlspecialchars(isset($fNom) ? $fNom : '', ENT_QUOTES, 'UTF-8'); ?>" required><br><br>

        <label for="taille" class="w3-fate7 w3-allerta">Taille de la Chambre (1, 2, 3 ou 4 personnes)</label>
        <input type="text" name="taille" id="taille_id" class="w3-input taille_c" onkeypress="ReKey(event)"
          value="<?php echo htmlspecialchars(isset($ftaille) ? $ftaille : '', ENT_QUOTES, 'UTF-8'); ?>" required><br><br>

        <label for="sejour" class="w3-fate7 w3-allerta">Durée du Séjour (entre 1 et 10 jours)</label>
        <input type="text" name="sejour" id="sejour_id" class="w3-input sejour_c" onkeypress="ReKey(event)"
          value="<?php echo htmlspecialchars(isset($fsejour) ? $fsejour : '', ENT_QUOTES, 'UTF-8'); ?>" required><br><br>

        <label for="commentaire" class="w3-fate7 w3-allerta">Commentaire (120 caractères max)</label>
        <input name="commentaire" id="commentaire_id" class="w3-input"
          value="<?php echo htmlspecialchars(isset($fcommentaire) ? $fcommentaire : '', ENT_QUOTES, 'UTF-8'); ?>" maxlength="120"><br><br>

        <button type="submit" class="w3-btn w3-fate77 w3-text-white w3-allerta">Envoyer</button>
        <br>
    </form>
  </div>
</div>

<?php
}
 ?>
