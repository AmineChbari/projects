
<script type="text/JavaScript">
        function Verif_taille()
        {
           var x = document.getElementById("taille_id").value;
           if (isNaN(x) || taille < 1 || taille > 4) 
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
        
        function Verif_sejour()
        {
           var x = document.getElementById("sejour_id").value;
           if (isNaN(x) == true) 
           {
               alert("Durée de séjour n'est pas valide / (1:10)");
               return false;
            } 
            else 
            {
              if (x>0 && x<=10) 
                {
                 return true;
                }
              else 
                {
                 alert("Durée de séjour n'est pas valide / (1:10)");
                 return false;
                }
             }
        }

        function Verif_commentaire()
        {
           var x = document.getElementById("commentaire_id").value;
           if (isNaN(x) == true) 
           {
               alert("commentaire n'est pas valide / (<120)");
               return false;
            } 
            else 
            {
              if (x>0 && x<=120) 
                {
                 return true;
                }
              else 
                {
                 alert("commentaire n'est pas valide / (<120)");
                 return false;
                }
             }
        }        
        
        function Subm() {
            return Verif_taille() && Verif_sejour() && Verif_commentaire();
        }
        
        function ReKey(event) {
             var unicode= event.which;
            if (unicode == 13) {return Subm();}
        }
</script>

<?php   

  $fichier_R = fopen("reservations.txt","r");
  $valide_id = false;
  $identifiant=0;
  $submit_data = "";
  while(!feof($fichier_R))
  {
    $data= fgets($fichier_R);
    
    if (trim($data) == ""){ 
      continue; 
    } 
    list($identifiant,$Nom,$taille,$sejour,$commentaire) = explode("|", $data);
    if (isset($_GET['identifiant'])) { 
      if ($identifiant != $_GET['identifiant']) {
        $submit_data .= $data;
      } else {
        if (isset($_POST['taille'])){
          $change_data= $_GET['identifiant'].'|'.$_POST['Nom'].'|'.$_POST['taille'].'|'.$_POST['sejour'].'|'.$_POST['commentaire']."\n";
          $submit_data .= $change_data;
          $valide_id = true;
        } else { 
          break;
        }
      }
    }
  }
  fclose($fichier_R);

  if (isset($_POST['taille'])){
    if (!$valide_id){
      $change_data = "\n".$_GET['identifiant'].'|'.$_POST['Nom'].'|'.$_POST['taille'].'|'.$_POST['sejour'].'|'.$_POST['commentaire']."\n";
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
      $Nom="";
      $taille="";
      $sejour="";
      $commentaire="";
     }
?>  



  <img src="IMG/r.jpg" alt="Lights" style="width:100%" class="w3-display-container">
  <br>
  <div class="w3-card-4 w3-display-container w3-margin w3-white  w3-animate-opacity w3-display-left" style="left:80px;">
    <header class="w3-allerta w3-black w3-display-top"><i>  Reservation   # <?php  echo $identifiant; ?> </i></header>
    <form class="w3-container w3-padding-32"  onsubmit="return Subm() " method="POST" action="index.php?page=modifresa.php&identifiant=<?php echo $identifiant; ?>">
      

        <label for="Nom" class="w3-fate7 w3-allerta">NOM</label> <input type="text" name="Nom" id="Nom"  class="w3-input" onkeypress="ReKey(event)" <?php if (isset($Nom)){echo "value = '$Nom'";} ?> required><br><br>

        <label for="taille" class="w3-fate7 w3-allerta">Taille de la Chambre (pour 1,2,3 ou 4 personnes)</label> <input type="text" name="taille" id="taille_id"  class="w3-input taille_c" onkeypress="ReKey(event)" <?php if (isset($taille)){echo "value = '$taille'";} ?> required><br><br>

        <label for="sejour" class="w3-fate7 w3-allerta">Durée du Séjour (entre 1 et 10 jours)</label> <input type="text" name="sejour" id="sejour_id"  class="w3-input sejour_c" onkeypress="ReKey(event)" <?php if (isset($sejour)){echo "value = '$sejour'";} ?>required ><br><br>

        <label for="commentaire" class="w3-fate7 w3-allerta">Commentaire</label> <input name="commentaire" id="commentaire_id" rows="3" class="w3-input" <?php if (isset($commentaire)){echo "value = '$commentaire'";} ?> required><br><br>

                <button type="submit" class="w3-btn w3-fate77 w3-text-white w3-allerta">Envoyer</button>
                <br>
    </form>
  </div>
</div> 

<?php 
}
 ?>