package fr.zeamateis.ice_age.client.particle.type;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class NumericParticleType extends ParticleType<NumericParticleType> implements IParticleData {
	public static final IDeserializer<NumericParticleType> DESERIALIZER = new IDeserializer<NumericParticleType>() {
		public NumericParticleType deserialize(ParticleType<NumericParticleType> particleTypeIn, StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			return new NumericParticleType(particleTypeIn.getId());
		}

		public NumericParticleType read(ParticleType<NumericParticleType> particleTypeIn, PacketBuffer buffer) {
			return new NumericParticleType(particleTypeIn.getId());
		}
	};

	private double number;
	private int color;

	public NumericParticleType(ResourceLocation resourceLocationIn) {
		super(resourceLocationIn, true, DESERIALIZER);
	}

	public void write(PacketBuffer buffer) {
		buffer.writeDouble(this.number);
		buffer.writeInt(this.color);
	}

	public String getParameters() {
		return this.getType().getId() + " " + this.number + " " + this.color;
	}

	public ParticleType<NumericParticleType> getType() {
		return this;
	}

	@OnlyIn(Dist.CLIENT)
	public double getNumber() {
		return this.number;
	}

	@OnlyIn(Dist.CLIENT)
	public int getColor() {
		return this.color;
	}

	public void setNumber(double numberIn) {
		this.number = numberIn;
	}

	public void setColor(int color) {
		this.color = color;
	}
}