package com.example.codesherlockai.domain.model

enum class InvestigationDepth(val displayName: String, val description: String) {
    QUICK("Quick", "Fast heuristic scan over recent PRs & commits"),
    STANDARD("Standard", "Full RAG retrieval across code, issues & PRs"),
    DEEP("Deep", "Exhaustive multi-agent analysis with call-graph tracing")
}
