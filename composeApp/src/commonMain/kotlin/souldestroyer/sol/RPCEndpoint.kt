package souldestroyer.sol

enum class RPCEndpoint(val url: String, val description: String) {
    DEV_NET("https://api.devnet.solana.com", "DevNet"),
    TEST_NET("https://api.testnet.solana.com", "TestNet"),
    MAIN_NET("https://api.mainnet-beta.solana.com", "MainNet Beta");
}