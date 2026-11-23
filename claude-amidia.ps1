function claude-amidia {
    $env:ANTHROPIC_BASE_URL = "http://127.0.0.1:31415"
    $env:ANTHROPIC_AUTH_TOKEN = "freellmapi-23ff2704626c3a5d80b498554e545411a7774483462fa20e"

    $claudeCli = Join-Path $HOME ".local\bin\claude.exe"
    if (-not (Test-Path -LiteralPath $claudeCli -PathType Leaf)) {
        throw "Claude CLI not found: $claudeCli"
    }

    & $claudeCli @args
}