package com.snobot.vision_app.app2018.detectors;

import com.snobot.vision_app.app2018.IDebugLogger;

public interface ITensorFlowDetectorFactory<RawImageTypeT>
{

    IDetector<RawImageTypeT> createDetector(IDebugLogger aLogger);

}
