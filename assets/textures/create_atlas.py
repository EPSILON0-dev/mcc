import sys
from pathlib import Path

try:
	from PIL import Image
except ImportError:
	print("Error: Pillow is not installed. Install it with: pip install Pillow")
	sys.exit(1)


IMAGE_NAMES = [
	"stone.png",
	"cobblestone.png",
	"bedrock.png",
	"dirt.png",
	"grass_block_side.png",
	"grass_block_top.png",
	"sand.png",
	"oak_log.png",
	"oak_log_top.png",
	"oak_leaves.png",
]
ATLAS_NAME = "atlas.png"
ATLAS_SIZE = 4
IMAGE_SIZE = 32
EMPTY_COLOR = (0xFF, 0x7F, 0x7F, 0xFF)


def main() -> None:
	texture_dir = Path(__file__).resolve().parent
	atlas_pixel_size = ATLAS_SIZE * IMAGE_SIZE
	atlas = Image.new("RGBA", (atlas_pixel_size, atlas_pixel_size), EMPTY_COLOR)
	usable_slots = (ATLAS_SIZE * ATLAS_SIZE) - 1

	if len(IMAGE_NAMES) > usable_slots:
		print(
			f"Error: ATLAS_SIZE={ATLAS_SIZE} only provides {usable_slots} usable slots because the first slot is reserved."
		)
		sys.exit(1)

	for slot_index, image_name in enumerate(IMAGE_NAMES, start=1):
		image_path = texture_dir / image_name
		if not image_path.exists():
			print(f"Error: Missing texture file: {image_name}")
			sys.exit(1)

		with Image.open(image_path) as image:
			tile = image.convert("RGBA")

		if tile.size != (IMAGE_SIZE, IMAGE_SIZE):
			print(
				f"Error: {image_name} has size {tile.size}, expected {(IMAGE_SIZE, IMAGE_SIZE)}"
			)
			sys.exit(1)

		x = (slot_index % ATLAS_SIZE) * IMAGE_SIZE
		y = (slot_index // ATLAS_SIZE) * IMAGE_SIZE
		atlas.paste(tile, (x, y))

	output_path = texture_dir / ATLAS_NAME
	atlas.save(output_path)
	print(f"Saved texture atlas to {output_path}")


if __name__ == "__main__":
	main()
