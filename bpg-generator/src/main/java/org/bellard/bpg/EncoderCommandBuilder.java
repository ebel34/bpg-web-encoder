package org.bellard.bpg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bellard.bpg.exception.TechnicalException;
import org.bellard.bpg.model.ExceptionData;

public class EncoderCommandBuilder {

	private String bpgEncoderCommandPath;
	private String inputFile;
	private String outputFile;
	private int quantizer = 28;
	private int chroma = 420;
	private String colorSpace = "ycbcr";
	private int level = 8;
	private int bitDepth = 8;

	private final Set<String> colorSpaces = new HashSet<String>(Arrays.asList("ycbcr", "rgb", "ycgco", "ycbcr_bt709",
			"ycbcr_bt2020"));

	public EncoderCommandBuilder(String bpgEncoderCommandPath, String inputFile, String outputFile) {
		super();
		this.bpgEncoderCommandPath = bpgEncoderCommandPath;
		this.inputFile = inputFile;
		this.outputFile = outputFile;
	}

	public EncoderCommandBuilder setQuantizer(int quantizer) {
		if (quantizer < 0 || quantizer > 51) {
			throw new TechnicalException(ExceptionData.BPG_COMMAND_PARAMETER_ERROR, "quantizer value incorrect : "
					+ quantizer);
		}
		this.quantizer = quantizer;
		return this;
	}

	public EncoderCommandBuilder setChroma(int chroma) {
		if (chroma != 420 && chroma != 422 && chroma != 444) {
			throw new TechnicalException(ExceptionData.BPG_COMMAND_PARAMETER_ERROR, "chroma incorrect : " + chroma);
		}
		this.chroma = chroma;
		return this;
	}

	public EncoderCommandBuilder setColorSpace(String cs) {
		String colorSpace = cs.trim();
		if (!colorSpaces.contains(colorSpace)) {
			throw new TechnicalException(ExceptionData.BPG_COMMAND_PARAMETER_ERROR, "colorspace incorrect : "
					+ colorSpace);
		}
		this.colorSpace = colorSpace;
		return this;
	}

	public EncoderCommandBuilder setCompression(int level) {
		if (level < 1 || level > 9) {
			throw new TechnicalException(ExceptionData.BPG_COMMAND_PARAMETER_ERROR, "compression level incorrect : "
					+ level);
		}
		this.level = level;
		return this;
	}

	public EncoderCommandBuilder setBitDepth(int level) {
		if (level < 8 || level > 12) {
			throw new TechnicalException(ExceptionData.BPG_COMMAND_PARAMETER_ERROR, "bit depth incorrect : " + level);
		}
		this.bitDepth = level;
		return this;
	}

	public List<String> build() {

		List<String> command = new ArrayList<String>();
		command.add(bpgEncoderCommandPath);
		command.add("-o");
		command.add(outputFile);

		command.add("-q");
		command.add("" + quantizer);

		command.add("-f");
		command.add("" + chroma);
		command.add("-c");
		command.add(colorSpace);
		command.add("-b");
		command.add("" + bitDepth);
		command.add("-m");
		command.add("" + level);
		command.add(inputFile);
		return command;
	}

}
