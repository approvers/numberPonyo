package types

case class Pokemon(
    name: String,
    link: String,
    calling: String,
    types: List[String],
    explanation: String,
    characteristic: List[String],
    img: String
  )
