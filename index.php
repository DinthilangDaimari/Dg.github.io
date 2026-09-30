<?php
declare(strict_types=1);

// ---------- Config ----------
$uploadDir = __DIR__ . '/uploads/';
$maxSize   = 5 * 1024 * 1024; // 5 MB
$allowed   = [
    'image/jpeg' => 'jpg',
    'image/png'  => 'png',
    'image/gif'  => 'gif',
    'image/webp' => 'webp',
];

// ---------- Setup uploads folder ----------
if (!is_dir($uploadDir)) {
    mkdir($uploadDir, 0755, true);
}
// Block script execution inside uploads (Apache)
$ht = $uploadDir . '.htaccess';
if (!file_exists($ht)) {
    file_put_contents($ht, "<FilesMatch \"\\.(php|phtml|phar)$\">\nRequire all denied\n</FilesMatch>\n");
}

// ---------- Handle upload ----------
$message = '';
$error   = '';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    if (!isset($_FILES['image']) || $_FILES['image']['error'] !== UPLOAD_ERR_OK) {
        $error = 'Upload failed. Please choose a file and try again.';
    } elseif ($_FILES['image']['size'] > $maxSize) {
        $error = 'File is too large (max 5 MB).';
    } else {
        $tmp   = $_FILES['image']['tmp_name'];
        $finfo = new finfo(FILEINFO_MIME_TYPE);
        $mime  = $finfo->file($tmp);

        if (!isset($allowed[$mime]) || getimagesize($tmp) === false) {
            $error = 'Only JPG, PNG, GIF, or WEBP images are allowed.';
        } else {
            $name = bin2hex(random_bytes(8)) . '.' . $allowed[$mime];
            if (move_uploaded_file($tmp, $uploadDir . $name)) {
                $message = 'Image uploaded successfully!';
            } else {
                $error = 'Could not save the file. Check folder permissions.';
