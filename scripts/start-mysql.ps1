# Local MySQL helper for cailu-bom (when Docker/WSL not ready)
# Usage: powershell -File scripts/start-mysql.ps1

$ErrorActionPreference = 'Continue'
$mysqlBin = 'C:\Program Files\MySQL\MySQL Server 8.4\bin'
$ini = 'C:\Tools\my.ini'

if (-not (Test-Path $ini)) {
  throw "Missing $ini — MySQL was not initialized for this project."
}

cmd /c "`"$mysqlBin\mysqladmin.exe`" -u root -proot --protocol=tcp ping >NUL 2>&1"
if ($LASTEXITCODE -eq 0) {
  Write-Host 'MySQL already running on 3306'
  exit 0
}

if (-not (Get-Process mysqld -ErrorAction SilentlyContinue)) {
  Start-Process -FilePath "$mysqlBin\mysqld.exe" -ArgumentList "--defaults-file=$ini" -WindowStyle Hidden
}

for ($i = 0; $i -lt 30; $i++) {
  Start-Sleep -Seconds 1
  cmd /c "`"$mysqlBin\mysqladmin.exe`" -u root -proot --protocol=tcp ping >NUL 2>&1"
  if ($LASTEXITCODE -eq 0) {
    Write-Host 'MySQL started (root/root @ 127.0.0.1:3306, db=cailu_bom)'
    exit 0
  }
}

throw 'Failed to start MySQL'
