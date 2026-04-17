<?php
header('Content-Type: application/json; charset=utf-8');
header('Access-Control-Allow-Origin: *');
header('Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS');
header('Access-Control-Allow-Headers: Content-Type');

if ($_SERVER['REQUEST_METHOD'] === 'OPTIONS') {
    http_response_code(204);
    exit;
}

$dataFile = __DIR__ . '/data.json';

function loadData(string $file): array {
    if (!file_exists($file)) { return []; }
    $content = file_get_contents($file);
    return json_decode($content, true) ?: [];
}

function saveData(string $file, array $data): void {
    file_put_contents($file, json_encode(array_values($data), JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE));
}

function validate(array $data): bool {
    $taille  = intval($data['taille']  ?? 0);
    $sejour  = intval($data['sejour']  ?? 0);
    $nom     = trim($data['nom']       ?? '');
    $comment = $data['commentaire']    ?? '';
    return $taille >= 1 && $taille <= 4
        && $sejour >= 1 && $sejour <= 10
        && strlen($nom) >= 2
        && strlen($comment) <= 120;
}

function sanitize(array $data): array {
    return [
        'id'          => intval($data['id']          ?? 0),
        'nom'         => htmlspecialchars(str_replace('|', '', trim($data['nom'] ?? '')), ENT_QUOTES, 'UTF-8'),
        'taille'      => intval($data['taille']       ?? 1),
        'sejour'      => intval($data['sejour']       ?? 1),
        'commentaire' => htmlspecialchars(str_replace('|', '', trim($data['commentaire'] ?? '')), ENT_QUOTES, 'UTF-8'),
        'chambre'     => htmlspecialchars(trim($data['chambre']     ?? ''), ENT_QUOTES, 'UTF-8'),
        'dateArrivee' => htmlspecialchars(trim($data['dateArrivee'] ?? ''), ENT_QUOTES, 'UTF-8'),
    ];
}

$method = $_SERVER['REQUEST_METHOD'];

switch ($method) {
    case 'GET':
        echo json_encode(loadData($dataFile));
        break;

    case 'POST':
        $input = json_decode(file_get_contents('php://input'), true) ?? [];
        if (!validate($input)) {
            http_response_code(400);
            echo json_encode(['error' => 'Données invalides.']);
            break;
        }
        $data  = loadData($dataFile);
        $maxId = empty($data) ? 0 : max(array_column($data, 'id'));
        $input['id'] = $maxId + 1;
        $data[] = sanitize($input);
        saveData($dataFile, $data);
        echo json_encode(end($data));
        break;

    case 'PUT':
        $id    = intval($_GET['id'] ?? 0);
        $input = json_decode(file_get_contents('php://input'), true) ?? [];
        if ($id <= 0 || !validate($input)) {
            http_response_code(400);
            echo json_encode(['error' => 'Données invalides.']);
            break;
        }
        $data  = loadData($dataFile);
        $found = false;
        foreach ($data as &$item) {
            if ($item['id'] === $id) {
                $input['id'] = $id;
                $item  = sanitize($input);
                $found = true;
                break;
            }
        }
        unset($item);
        if (!$found) { http_response_code(404); echo json_encode(['error' => 'Introuvable.']); break; }
        saveData($dataFile, $data);
        echo json_encode(['success' => true]);
        break;

    case 'DELETE':
        $id   = intval($_GET['id'] ?? 0);
        $data = loadData($dataFile);
        $data = array_values(array_filter($data, fn($item) => $item['id'] !== $id));
        saveData($dataFile, $data);
        echo json_encode(['success' => true]);
        break;

    default:
        http_response_code(405);
        echo json_encode(['error' => 'Méthode non autorisée.']);
}
