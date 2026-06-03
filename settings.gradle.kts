rootProject.name = "api"

include(":bootstrap")
include(":shared:kernel")
include(":shared:infrastructure")

// question
include(":question:domain")
include(":question:port")
include(":question:application")
include(":question:adapter:web")
include(":question:adapter:persistence")

// question_generation
include(":question_generation:domain")
include(":question_generation:port")
include(":question_generation:application")
include(":question_generation:adapter:web")
include(":question_generation:adapter:persistence")
include(":question_generation:adapter:ai")
include(":question_generation:adapter:domain_connector")

// exam
include(":exam:domain")
include(":exam:port")
include(":exam:application")
include(":exam:adapter:web")
include(":exam:adapter:persistence")
include(":exam:adapter:domain_connector")

// token_usage
include(":token_usage:domain")
include(":token_usage:port")
include(":token_usage:application")
include(":token_usage:adapter:web")
include(":token_usage:adapter:persistence")

// auth
include(":auth")

//member
include(":member")
include("member:domain")
include("member:application")
include("member:application")
include("member:adapter")
include("member:adapter:persistence")
include("member:adapter:web")
include("member:port")