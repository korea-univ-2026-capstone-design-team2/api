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

// auth
include(":auth")

//member
include(":member")
