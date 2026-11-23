$env:Path = "$HOME\.local\bin;$env:Path"

function claude {
  $env:ANTHROPIC_BASE_URL = "http://127.0.0.1:31415"
  $env:ANTHROPIC_AUTH_TOKEN = "freellmapi-23ff2704626c3a5d80b498554e545411a7774483462fa20e"
  $claudeCli = Get-Command -Name 'claude.exe', 'claude.cmd', 'claude.ps1' `
    -CommandType Application, ExternalScript -ErrorAction SilentlyContinue |
    Select-Object -First 1

  if (-not $claudeCli) {
    throw "Claude CLI executable was not found on PATH."
  }
  & $claudeCli.Source @args
}