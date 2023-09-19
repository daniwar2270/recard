export interface CardResponse {
  id: number,
  name: string,
  rarity: string,
  attack: number,
  defence: number,
  description: string,
  imageUrl?: string,
  thumbnailUrl?: string
}
